package ru.akvine.iskra.rest.dto.process;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class TableProcessDto {
    private String pid;
    private String tableName;
    private long successRowsCount;
    private String processState;
    private Date completedDate;
    private String errorMessage;
}
