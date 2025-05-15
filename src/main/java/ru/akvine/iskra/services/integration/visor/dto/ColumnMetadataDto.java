package ru.akvine.iskra.services.integration.visor.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ColumnMetadataDto {
    private String database;
    private String schemaName;
    private String columnName;
    private String tableName;
    private String dataType;
    private int orderIndex;
    private int size;
    private boolean generatedAlways;
    private boolean primaryKey;
}
