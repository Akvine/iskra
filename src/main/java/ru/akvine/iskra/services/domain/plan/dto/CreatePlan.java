package ru.akvine.iskra.services.domain.plan.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreatePlan {
    private String userUuid;
    private String name;
    private String connectionName;
}
