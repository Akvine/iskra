package ru.akvine.iskra.services.domain.plan.dto;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.PlanState;

@Data
@Accessors(chain = true)
public class UpdatePlan {
    private String planUuid;
    private String userUuid;

    @Nullable
    private PlanState planState;

    @Nullable
    private String lastProcessUuid;
}
