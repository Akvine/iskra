package ru.akvine.iskra.services.dto.plan.action;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StartAction {
    private String userUuid;
    private String planUuid;
    private boolean resume;
}
