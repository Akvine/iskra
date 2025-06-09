package ru.akvine.iskra.rest.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.components.SecurityManager;
import ru.akvine.iskra.rest.dto.plan.CreatePlanRequest;
import ru.akvine.iskra.rest.dto.plan.DuplicatePlanRequest;
import ru.akvine.iskra.rest.dto.plan.PlanDto;
import ru.akvine.iskra.rest.dto.plan.PlanListResponse;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.dto.plan.CreatePlan;
import ru.akvine.iskra.services.dto.plan.DuplicatePlan;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PlanMapper {
    private final SecurityManager securityManager;

    public PlanListResponse convertToProcessListResponse(List<PlanModel> processes) {
        Asserts.isNotNull(processes);
        return new PlanListResponse()
                .setPlans(processes.stream().map(this::buildPlanDto).toList());
    }

    public CreatePlan convertToCreatePlan(CreatePlanRequest request) {
        Asserts.isNotNull(request);
        return new CreatePlan()
                .setUserUuid(securityManager.getCurrentUser().getUuid())
                .setName(request.getName())
                .setConnectionName(request.getConnectionName());
    }

    public DuplicatePlan convertToDuplicatePlan(DuplicatePlanRequest request) {
        Asserts.isNotNull(request);
        return new DuplicatePlan()
                .setUuid(request.getUuid())
                .setCopyResults(request.isCopyResults())
                .setUserUuid(securityManager.getCurrentUser().getUuid());
    }

    private PlanDto buildPlanDto(PlanModel plan) {
        return new PlanDto()
                .setLastProcessUuid(plan.getLastProcessUuid())
                .setUuid(plan.getUuid())
                .setName(plan.getName());
    }
}
