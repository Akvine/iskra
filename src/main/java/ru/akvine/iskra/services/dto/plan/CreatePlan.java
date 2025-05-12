package ru.akvine.iskra.services.dto.plan;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreatePlan {
    private String name;
    private String connectionName;
}
