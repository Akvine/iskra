package ru.akvine.iskra.rest.converter;

import org.springframework.stereotype.Component;
import ru.akvine.iskra.rest.dto.plan.actions.StartPlanResponse;

@Component
public class PlanActionsConverter {

    public StartPlanResponse convertToStartPlanResponse(String processUuid) {
        return new StartPlanResponse().setProcessUuid(processUuid);
    }
}
