package ru.akvine.iskra.rest.dto.process;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TableProcessDto {
    private String processUuid;
    private String pid;
    private String tableName;
    private long successRowsCount;
    private long totalRowsCount;
    private int successRowsPercent;
    private String processState;
    private Date completedDate;
    private String errorMessage;
}
