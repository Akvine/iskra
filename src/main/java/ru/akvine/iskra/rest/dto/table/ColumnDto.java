package ru.akvine.iskra.rest.dto.table;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ColumnDto {
    private String uuid;
    private String columnName;
    private boolean selected;
    private String rawDataType;
    private int orderIndex;
    private int size;
    private boolean generatedAlways;
    private boolean primaryKey;
    private String database;
    private String schemaName;
}
