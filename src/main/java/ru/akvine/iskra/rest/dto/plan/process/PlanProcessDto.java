package ru.akvine.iskra.rest.dto.plan.process;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

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
