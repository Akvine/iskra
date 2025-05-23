package ru.akvine.iskra.rest.converter;

import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.rest.dto.plan.CreatePlanRequest;
import ru.akvine.iskra.rest.dto.plan.GenerateDataRequest;
import ru.akvine.iskra.rest.dto.plan.PlanDto;
import ru.akvine.iskra.rest.dto.plan.PlanListResponse;
import ru.akvine.iskra.services.domain.PlanModel;
import ru.akvine.iskra.services.dto.GenerateDataAction;
import ru.akvine.iskra.services.dto.plan.CreatePlan;

import java.util.List;

@Component
public class PlanConverter {
    public PlanListResponse convertToProcessListResponse(List<PlanModel> processes) {
        Asserts.isNotNull(processes);
        return new PlanListResponse()
                .setProcesses(processes.stream().map(this::buildPlanDto).toList());
    }

    public CreatePlan convertToCreatePlan(CreatePlanRequest request) {
        Asserts.isNotNull(request);
        return new CreatePlan()
                .setName(request.getName())
                .setConnectionName(request.getConnectionName());
    }

    public GenerateDataAction convertToGenerateDataAction(String planUuid, GenerateDataRequest request) {
        return new GenerateDataAction()
                .setPlanUuid(planUuid)
                .setRelationsMatrix(request.getRelationsMatrix());
    }

    private PlanDto buildPlanDto(PlanModel plan) {
        return new PlanDto()
                .setLastProcessUuid(plan.getLastProcessUuid())
                .setUuid(plan.getUuid())
                .setName(plan.getName());
    }
}
