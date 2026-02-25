package ru.akvine.iskra.rest.dto.plan.process;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PlanProcessDto {
    private String uuid;
    private String state;
    private int totalTablesCount;
    private Date startedDate;
    private Date completedDate;
    private String errorMessage;
}
