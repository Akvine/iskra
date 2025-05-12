package ru.akvine.iskra.services.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TableMetadataDto {
    private String tableName;
    private String schema;
    private String database;
}
