package ru.akvine.iskra.rest.mappers;

import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.rest.dto.column.ListColumnResponse;
import ru.akvine.iskra.rest.dto.column.SelectColumnRequest;
import ru.akvine.iskra.rest.dto.table.ColumnDto;
import ru.akvine.iskra.services.domain.column.ColumnModel;
import ru.akvine.iskra.services.domain.column.dto.SelectColumn;

import java.util.List;

@Component
public class ColumnMapper {
    public SelectColumn convertToSelectColumn(SelectColumnRequest request) {
        Asserts.isNotNull(request);
        return new SelectColumn()
                .setPlanUuid(request.getPlanUuid())
                .setTableName(request.getTableName())
                .setSelected(request.getSelected());
    }

    public ListColumnResponse convertToListColumnResponse(List<ColumnModel> columns) {
        return new ListColumnResponse().setColumns(columns.stream().map(this::buildColumnDto).toList());
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
