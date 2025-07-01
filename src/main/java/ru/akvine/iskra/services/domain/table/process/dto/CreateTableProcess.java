package ru.akvine.iskra.services.domain.table.process.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateTableProcess {
    private String userUuid;
    private String planUuid;
    private String processUuid;
    private String tableName;
    private long totalRowsCount;
}
