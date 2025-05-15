package ru.akvine.iskra.rest.dto.table;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TableDto {
    private String tableName;
    private List<ColumnDto> columns;
}
