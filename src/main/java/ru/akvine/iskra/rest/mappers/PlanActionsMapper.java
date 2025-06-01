package ru.akvine.iskra.rest.mappers;

import org.springframework.stereotype.Component;
import ru.akvine.iskra.rest.dto.plan.actions.StartPlanResponse;

@Component
public class PlanActionsMapper {

    public StartPlanResponse convertToStartPlanResponse(String processUuid) {
        return new StartPlanResponse().setProcessUuid(processUuid);
    }
}
