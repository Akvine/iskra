package ru.akvine.iskra.rest.dto.table;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TableDto {
    private String tableName;
    private boolean selected;
    private List<ColumnDto> columns;
}
