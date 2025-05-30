package ru.akvine.iskra.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.converter.PlanActionsConverter;
import ru.akvine.iskra.rest.meta.plan.PlanActionsControllerMeta;
import ru.akvine.iskra.services.PlanActionService;

@RestController
@RequiredArgsConstructor
public class PlanActionsController implements PlanActionsControllerMeta {
    private final PlanActionService planActionService;
    private final PlanActionsConverter planActionsConverter;

    @Override
    public Response start(String planUuid) {
        String processUuid = planActionService.start(planUuid);
        return planActionsConverter.convertToStartPlanResponse(processUuid);
    }

    @Override
    public Response stop(String planUuid) {
        throw new UnsupportedOperationException("Method is not supported!");
    }
}
