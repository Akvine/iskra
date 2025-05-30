package ru.akvine.iskra.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.converter.PlanConverter;
import ru.akvine.iskra.rest.dto.plan.CreatePlanRequest;
import ru.akvine.iskra.rest.meta.plan.PlanControllerMeta;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.dto.plan.CreatePlan;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class PlanController implements PlanControllerMeta {
    private final PlanService planService;
    private final PlanConverter planConverter;

    @Override
    public Response list() {
        List<PlanModel> processes = planService.list();
        return planConverter.convertToProcessListResponse(processes);
    }

    @Override
    public Response create(@RequestBody @Valid CreatePlanRequest request) {
        CreatePlan createPlan = planConverter.convertToCreatePlan(request);
        PlanModel createdPlan = planService.create(createPlan);
        return planConverter.convertToProcessListResponse(List.of(createdPlan));
    }
}
