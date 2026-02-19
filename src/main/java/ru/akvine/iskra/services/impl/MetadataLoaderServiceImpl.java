package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.compozit.commons.utils.UUIDGenerator;
import ru.akvine.iskra.enums.ConstraintType;
import ru.akvine.iskra.enums.RelationShipType;
import ru.akvine.iskra.repositories.TableRepository;
import ru.akvine.iskra.repositories.entities.ColumnEntity;
import ru.akvine.iskra.repositories.entities.PlanEntity;
import ru.akvine.iskra.repositories.entities.TableEntity;
import ru.akvine.iskra.repositories.entities.embaddable.ReferenceInfo;
import ru.akvine.iskra.services.MetadataLoaderService;
import ru.akvine.iskra.services.domain.column.ColumnService;
import ru.akvine.iskra.services.domain.connection.ConnectionModel;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.domain.table.TableService;
import ru.akvine.iskra.services.integration.visor.VisorService;
import ru.akvine.iskra.services.integration.visor.dto.ColumnMetadataDto;
import ru.akvine.iskra.services.integration.visor.dto.LoadConstraintsResult;
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
                    .setSelected(true)
                    .setPlan(plan);
            TableEntity savedTable = tableService.save(tableToCreate);
            List<ColumnMetadataDto> columnsMetadata = visorService.loadColumns(tableToCreate.getName(), connection);

            List<ColumnEntity> columnsToSave = columnsMetadata.stream()
                    .map(column -> {
                        LoadConstraintsResult result = visorService.loadConstraints(table.getTableName(), column.getColumnName(), connection);
                        ColumnEntity columnToSave = new ColumnEntity()
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
                                .setConstraintTypes(result.getConstraintTypes());

                        if (result.getConstraintTypes().contains(ConstraintType.FOREIGN_KEY)) {
                            ReferenceInfo referenceInfo = new ReferenceInfo()
                                    .setRelationShipType(RelationShipType.ONE_TO_MANY)
                                    .setTargetTableNameForForeignKey(result.getTargetTableNameForForeignKey())
                                    .setTargetColumnNameForForeignKey(result.getTargetColumnNameForForeignKey());
                            columnToSave.setReferenceInfo(referenceInfo);
                        }

                        return columnToSave;
                    }).toList();
            columnService.saveAll(columnsToSave);
        }

        return tableRepository.findAll(planUuid, userUuid).stream()
                .map(TableModel::new)
                .toList();
    }

}
