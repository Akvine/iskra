package ru.akvine.iskra.rest.converter;

import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.rest.dto.table.ColumnDto;
import ru.akvine.iskra.rest.dto.table.ListTablesResponse;
import ru.akvine.iskra.rest.dto.table.TableDto;
import ru.akvine.iskra.services.domain.ColumnModel;
import ru.akvine.iskra.services.domain.TableModel;

import java.util.List;


@Component
public class TableConverter {
    public ListTablesResponse convertToListTablesResponse(List<TableModel> tables) {
        Asserts.isNotNull(tables);
        return new ListTablesResponse()
                .setTables(tables.stream().map(this::buildTableDto).toList());
    }

    private TableDto buildTableDto(TableModel table) {
        return new TableDto()
                .setTableName(table.getTableName())
                .setSelected(table.isSelected())
                .setColumns(table.getColumns().stream().map(this::buildColumnDto).toList());
    }

    private ColumnDto buildColumnDto(ColumnModel column) {
        return new ColumnDto()
                .setUuid(column.getUuid())
                .setColumnName(column.getColumnName())
                .setSelected(column.isSelected())
                .setDatabase(column.getDatabase())
                .setSize(column.getSize())
                .setPrimaryKey(column.isPrimaryKey())
                .setGeneratedAlways(column.isGeneratedAlways())
                .setOrderIndex(column.getOrderIndex())
                .setSchemaName(column.getSchemaName())
                .setRawDataType(column.getRawDataType());
    }
}
