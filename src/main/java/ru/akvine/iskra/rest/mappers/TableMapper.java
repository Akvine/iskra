package ru.akvine.iskra.rest.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.components.SecurityManager;
import ru.akvine.iskra.enums.ConstraintType;
import ru.akvine.iskra.rest.dto.table.*;
import ru.akvine.iskra.services.domain.column.ColumnModel;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.domain.table.dto.ListTables;
import ru.akvine.iskra.services.domain.table.dto.ToogleSelectedTables;

import java.util.List;


@Component
@RequiredArgsConstructor
public class TableMapper {
    private final SecurityManager securityManager;

    public ListTablesResponse mapToListTablesResponse(List<TableModel> tables) {
        Asserts.isNotNull(tables);
        return new ListTablesResponse()
                .setTables(tables.stream().map(this::buildTableDto).toList());
    }

    public ToogleSelectedTables mapToToggleSelectedTables(ToggleSelectedRequest request) {
        Asserts.isNotNull(request);
        return new ToogleSelectedTables()
                .setPlanUuid(request.getPlanUuid())
                .setToggled(request.getToggled());
    }

    public ListTables mapToListTables(ListTablesRequest request) {
        Asserts.isNotNull(request);
        return new ListTables()
                .setSelected(request.getSelected())
                .setUserUuid(securityManager.getCurrentUser().getUuid())
                .setPlanUuid(request.getPlanUuid());
    }

    private TableDto buildTableDto(TableModel table) {
        return new TableDto()
                .setTableName(table.getTableName())
                .setSelected(table.isSelected())
                .setColumns(table.getColumns().stream().map(this::buildColumnDto).toList());
    }

    private ColumnDto buildColumnDto(ColumnModel column) {
        ColumnDto columnDto = new ColumnDto()
                .setUuid(column.getUuid())
                .setColumnName(column.getColumnName())
                .setSelected(column.isSelected())
                .setDatabase(column.getDatabase())
                .setSize(column.getSize())
                .setPrimaryKey(column.isPrimaryKey())
                .setGeneratedAlways(column.isGeneratedAlways())
                .setOrderIndex(column.getOrderIndex())
                .setSchemaName(column.getSchemaName())
                .setRawDataType(column.getRawDataType())
                .setConstraints(column.getConstraints().stream().map(ConstraintType::getName).toList());
        if (column.getConstraints().contains(ConstraintType.FOREIGN_KEY)) {
            ReferenceInfoDto referenceInfoDto = new ReferenceInfoDto()
                    .setTargetTableNameForForeignKey(column.getTargetTableNameForForeignKey())
                    .setTargetColumnNameForForeignKey(column.getTargetColumnNameForForeignKey())
                    .setRelationsShipType(column.getRelationShipType().toString());
            columnDto.setReferenceInfo(referenceInfoDto);
        }

        return columnDto;
    }
}
