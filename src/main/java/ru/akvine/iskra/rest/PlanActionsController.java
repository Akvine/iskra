package ru.akvine.iskra.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;
import ru.akvine.iskra.rest.mappers.PlanActionsMapper;
import ru.akvine.iskra.rest.meta.plan.PlanActionsControllerMeta;
import ru.akvine.iskra.services.PlanActionService;

@RestController
@RequiredArgsConstructor
public class PlanActionsController implements PlanActionsControllerMeta {
    private final PlanActionService planActionService;
    private final PlanActionsMapper planActionsMapper;

    @Override
    public Response start(String planUuid) {
        String processUuid = planActionService.start(planUuid, false);
        return planActionsMapper.convertToStartPlanResponse(processUuid);
    }

    @Override
    public Response stop(String planUuid) {
        planActionService.stop(planUuid);
        return new SuccessfulResponse();
    }

    @Override
    public Response resume(String planUuid) {
        String processUuid = planActionService.resume(planUuid);
        return planActionsMapper.convertToStartPlanResponse(processUuid);
    }
}
