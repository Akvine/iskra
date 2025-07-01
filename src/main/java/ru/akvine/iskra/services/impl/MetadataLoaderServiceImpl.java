package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.compozit.commons.utils.UUIDGenerator;
import ru.akvine.iskra.enums.ConstraintType;
import ru.akvine.iskra.exceptions.table.TablesNotLoadedException;
import ru.akvine.iskra.repositories.TableRepository;
import ru.akvine.iskra.repositories.dto.RelationsMatrix;
import ru.akvine.iskra.repositories.entities.ColumnEntity;
import ru.akvine.iskra.repositories.entities.PlanEntity;
import ru.akvine.iskra.repositories.entities.TableEntity;
import ru.akvine.iskra.services.MetadataLoaderService;
import ru.akvine.iskra.services.domain.column.ColumnService;
import ru.akvine.iskra.services.domain.connection.ConnectionModel;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.domain.table.TableService;
import ru.akvine.iskra.services.domain.plan.dto.UpdatePlan;
import ru.akvine.iskra.services.integration.visor.VisorService;
import ru.akvine.iskra.services.integration.visor.dto.ColumnMetadataDto;
import ru.akvine.iskra.services.integration.visor.dto.TableMetadataDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetadataLoaderServiceImpl implements MetadataLoaderService {
    private final VisorService visorService;

    private final PlanService planService;
    private final TableService tableService;
    private final ColumnService columnService;
    private final TableRepository tableRepository;

    @Override
    public List<TableModel> loadOrList(String planUuid, String userUuid) {
        Asserts.isNotNull(planUuid);

        PlanEntity plan = planService.verifyExists(planUuid, userUuid);

        List<TableModel> tables = tableService.getAll(planUuid, userUuid);
        if (!tables.isEmpty()) {
            return tables;
        }

        ConnectionModel connection = new ConnectionModel(plan.getConnection());
        List<TableMetadataDto> tablesMetadata = visorService.loadTables(connection);
        for (TableMetadataDto table : tablesMetadata) {
            TableEntity tableToCreate = new TableEntity()
                    .setName(table.getTableName())
                    .setSchema(table.getSchema())
                    .setDatabase(table.getDatabase())
                    .setPlan(plan);
            TableEntity savedTable = tableService.save(tableToCreate);
            List<ColumnMetadataDto> columnsMetadata = visorService.loadColumns(tableToCreate.getName(), connection);

            List<ColumnEntity> columnsToSave = columnsMetadata.stream()
                    .map(column -> {
                        List<ConstraintType> constraints = visorService.loadConstraints(table.getTableName(), column.getColumnName(), connection);
                        return new ColumnEntity()
                                .setUuid(UUIDGenerator.uuidWithoutDashes())
                                .setColumnName(column.getColumnName())
                                .setTable(savedTable)
                                .setSize(column.getSize())
                                .setOrderIndex(column.getOrderIndex())
                                .setRawDataType(column.getDataType())
                                .setPrimaryKey(column.isPrimaryKey())
                                .setGeneratedAlways(column.isGeneratedAlways())
                                .setDatabase(column.getDatabase())
                                .setSchemaName(column.getSchemaName())
                                .setConstraintTypes(constraints);
                    }).toList();
            columnService.saveAll(columnsToSave);
        }

        return tableRepository.findAll(planUuid, userUuid).stream()
                .map(TableModel::new)
                .toList();
    }

    @Override
    public RelationsMatrix generate(String planUuid, String userUuid) {
        Asserts.isNotBlank(planUuid, "planUuid is blank");
        Asserts.isNotBlank(userUuid, "userUuid is blank");

        PlanEntity plan = planService.verifyExists(planUuid, userUuid);
        if (plan.getRelationsMatrix() != null) {
            return plan.getRelationsMatrix();
        }

        List<String> tableNames = tableService.getAll(planUuid, userUuid).stream()
                .map(TableModel::getTableName).toList();
        if (CollectionUtils.isEmpty(tableNames)) {
            String errorMessage = String.format(
                    "For plan with uuid = [%s] not loaded any tables!",
                    planUuid
            );
            throw new TablesNotLoadedException(errorMessage);
        }

        RelationsMatrix matrix = new RelationsMatrix(tableNames);
        log.info("Start generate relations matrix for plan with uuid = [{}]", plan.getUuid());

        ConnectionModel connection = new ConnectionModel(plan.getConnection());
        for (String tableName : tableNames) {
            List<String> relatedTableNames = visorService.getRelatedTables(tableName, connection);
            if (CollectionUtils.isEmpty(relatedTableNames)) {
                continue;
            }

            for (String relatedTableName : relatedTableNames) {
                matrix.getByColumn(tableName).replace(relatedTableName, true);
            }
        }

        log.info("Successful generate relations matrix for plan with uuid = [{}]", plan.getUuid());
        UpdatePlan updatePlan = new UpdatePlan()
                .setPlanUuid(planUuid)
                .setUserUuid(userUuid)
                .setRelationsMatrix(matrix);

        planService.update(updatePlan);
        return matrix;
    }
}
