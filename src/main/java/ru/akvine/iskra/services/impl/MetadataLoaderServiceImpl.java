package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.compozit.commons.utils.UUIDGenerator;
import ru.akvine.iskra.enums.ConstraintType;
import ru.akvine.iskra.repositories.TableRepository;
import ru.akvine.iskra.repositories.entities.ColumnEntity;
import ru.akvine.iskra.repositories.entities.PlanEntity;
import ru.akvine.iskra.repositories.entities.TableEntity;
import ru.akvine.iskra.services.ColumnService;
import ru.akvine.iskra.services.MetadataLoaderService;
import ru.akvine.iskra.services.PlanService;
import ru.akvine.iskra.services.TableService;
import ru.akvine.iskra.services.domain.ConnectionModel;
import ru.akvine.iskra.services.domain.TableModel;
import ru.akvine.iskra.services.integration.visor.VisorService;
import ru.akvine.iskra.services.integration.visor.dto.ColumnMetadataDto;
import ru.akvine.iskra.services.integration.visor.dto.TableMetadataDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetadataLoaderServiceImpl implements MetadataLoaderService {
    private final VisorService visorService;

    private final PlanService planService;
    private final TableService tableService;
    private final ColumnService columnService;
    private final TableRepository tableRepository;

    @Override
    public List<TableModel> loadOrList(String planUuid) {
        Asserts.isNotNull(planUuid);

        PlanEntity plan = planService.verifyExists(planUuid);

        List<TableModel> tables = tableService.getAll(planUuid);
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

        return tableRepository.findAll(planUuid).stream()
                .map(TableModel::new)
                .toList();
    }
}
