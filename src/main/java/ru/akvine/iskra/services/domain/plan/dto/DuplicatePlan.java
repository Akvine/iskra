package ru.akvine.iskra.services.domain.plan.dto;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DuplicatePlan {
    private String userUuid;
    private String uuid;
    @Nullable
    private String name;
    private boolean copyResults;
}
