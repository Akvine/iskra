package ru.akvine.iskra.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;
import ru.akvine.iskra.rest.dto.plan.actions.StartPlanRequest;
import ru.akvine.iskra.rest.mappers.PlanActionsMapper;
import ru.akvine.iskra.rest.meta.plan.PlanActionsControllerMeta;
import ru.akvine.iskra.services.PlanActionService;
import ru.akvine.iskra.services.dto.plan.action.StartAction;

@RestController
@RequiredArgsConstructor
public class PlanActionsController implements PlanActionsControllerMeta {
    private final PlanActionService planActionService;
    private final PlanActionsMapper planActionsMapper;

    @Override
    public Response start(StartPlanRequest request) {
        StartAction action = planActionsMapper.mapToStartAction(request);
        String processUuid = planActionService.start(action);
        return planActionsMapper.convertToStartPlanResponse(processUuid);
    }

    @Override
    public Response stop(String planUuid) {
        planActionService.stop(planUuid);
        return new SuccessfulResponse();
    }
}
