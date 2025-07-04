package ru.akvine.iskra.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.components.SecurityManager;
import ru.akvine.iskra.rest.dto.plan.DuplicatePlanRequest;
import ru.akvine.iskra.rest.mappers.PlanMapper;
import ru.akvine.iskra.rest.dto.plan.CreatePlanRequest;
import ru.akvine.iskra.rest.meta.plan.PlanControllerMeta;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.domain.plan.dto.CreatePlan;
import ru.akvine.iskra.services.domain.plan.dto.DuplicatePlan;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class PlanController implements PlanControllerMeta {
    private final PlanService planService;
    private final PlanMapper planMapper;
    private final SecurityManager securityManager;

    @Override
    public Response list() {
        List<PlanModel> processes = planService.list(securityManager.getCurrentUser().getUuid());
        return planMapper.mapToProcessListResponse(processes);
    }

    @Override
    public Response create(@RequestBody @Valid CreatePlanRequest request) {
        CreatePlan createPlan = planMapper.mapToCreatePlan(request);
        PlanModel createdPlan = planService.create(createPlan);
        return planMapper.mapToProcessListResponse(List.of(createdPlan));
    }

    @Override
    public Response duplicate(DuplicatePlanRequest request) {
        DuplicatePlan action = planMapper.mapToDuplicatePlan(request);
        PlanModel plan = planService.duplicate(action);
        return planMapper.mapToProcessListResponse(List.of(plan));
    }
}
