package ru.akvine.iskra.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;
import ru.akvine.iskra.rest.converter.PlanConverter;
import ru.akvine.iskra.rest.dto.plan.CreatePlanRequest;
import ru.akvine.iskra.rest.dto.plan.GenerateDataRequest;
import ru.akvine.iskra.rest.meta.PlanControllerMeta;
import ru.akvine.iskra.services.PlanService;
import ru.akvine.iskra.services.domain.PlanModel;
import ru.akvine.iskra.services.dto.GenerateDataAction;
import ru.akvine.iskra.services.dto.plan.CreatePlan;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class PlanController implements PlanControllerMeta {
    private final PlanService planService;
    private final PlanConverter planConverter;

    @Override
    public Response start(@PathVariable("uuid") String planUuid,
                          @RequestBody @Valid GenerateDataRequest request) {
        GenerateDataAction action = planConverter.convertToGenerateDataAction(planUuid, request);
        planService.start(action);
        return new SuccessfulResponse();
    }

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
