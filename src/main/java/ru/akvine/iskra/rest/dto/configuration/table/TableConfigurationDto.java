package ru.akvine.iskra.rest.dto.configuration.table;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TableConfigurationDto {
    private String name;
    private int rowsCount;
    private int batchSize;
}
