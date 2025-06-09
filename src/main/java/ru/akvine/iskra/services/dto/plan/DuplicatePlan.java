package ru.akvine.iskra.services.dto.plan;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DuplicatePlan {
    private String userUuid;
    private String uuid;
    private boolean copyResults;
}
