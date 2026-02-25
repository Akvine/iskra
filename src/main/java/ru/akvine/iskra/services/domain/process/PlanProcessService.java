package ru.akvine.iskra.services.domain.process;

import jakarta.annotation.Nullable;
import ru.akvine.iskra.repositories.entities.PlanProcessEntity;
import ru.akvine.iskra.services.domain.process.dto.CreatePlanProcess;

public interface PlanProcessService {
    PlanProcessModel create(CreatePlanProcess action);

    PlanProcessEntity verifyExistsBy(String processUuid);

    PlanProcessModel getLastStoppedOrFailed(String planUuid);

    PlanProcessModel start(PlanProcessModel planProcess);

    PlanProcessModel toFail(PlanProcessModel planProcess, String errorMessage);

    PlanProcessModel toCompleted(PlanProcessModel planProcessModel);

    @Nullable
    PlanProcessModel getOrNull(String processUuid);

    PlanProcessModel get(String processUuid);
}
