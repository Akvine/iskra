package ru.akvine.iskra.services.dto.configuration.table;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateTableConfiguration {
    private String tableName;
    private String name;
    private int rowsCount;
    private int batchSize;
}
