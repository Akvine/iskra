package ru.akvine.iskra.rest.dto.plan;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class PlanDto {
    private String uuid;
    private String name;
    private String lastProcessUuid;
    private String state;
    private Date createdDate;
    private Date updatedDate;
    private ConstraintsSettingsInfo constraintsSettingsInfo;
}
