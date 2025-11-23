package ru.akvine.iskra.services.domain.plan.dto;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.repositories.dto.RelationsMatrix;

@Data
@Accessors(chain = true)
public class UpdatePlan {
    private String planUuid;
    private String userUuid;

    @Nullable
    private PlanState planState;

    @Nullable
    private String lastProcessUuid;
    @Nullable
    private RelationsMatrix relationsMatrix;
}
