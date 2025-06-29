package ru.akvine.iskra.rest.dto.plan;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PlanDto {
    private String uuid;
    private String name;
    private String lastProcessUuid;
    private ConstraintsSettingsInfo constraintsSettingsInfo;
}
