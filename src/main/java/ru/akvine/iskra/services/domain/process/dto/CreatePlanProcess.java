package ru.akvine.iskra.services.domain.process.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreatePlanProcess {
    private String planUuid;
    private String processUuid;
    private int totalTablesCount;
}
