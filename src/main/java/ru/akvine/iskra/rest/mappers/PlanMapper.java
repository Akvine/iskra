package ru.akvine.iskra.rest.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.components.SecurityManager;
import ru.akvine.iskra.rest.dto.plan.*;
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
                .setName(request.getName())
                .setCopyResults(request.isCopyResults())
                .setUserUuid(securityManager.getCurrentUser().getUuid());
    }

    private PlanDto buildPlanDto(PlanModel plan) {
        return new PlanDto()
                .setLastProcessUuid(plan.getLastProcessUuid())
                .setUuid(plan.getUuid())
                .setName(plan.getName())
                .setConstraintsSettingsInfo(buildConstraintsSettingsInfo(plan));
    }

    private ConstraintsSettingsInfo buildConstraintsSettingsInfo(PlanModel plan) {
        return new ConstraintsSettingsInfo()
                .setGenerateScriptsForNotNull(plan.isGenerateScriptsForNotNull())
                .setGenerateScriptsForIndex(plan.isGenerateScriptsForIndex())
                .setGenerateScriptsForPrimaryKey(plan.isGenerateScriptsForPrimaryKey())
                .setGenerateScriptsForTrigger(plan.isGenerateScriptsForTrigger())
                .setGenerateScriptsForUnique(plan.isGenerateScriptsForUnique())
                .setGenerateScriptsForCheck(plan.isGenerateScriptsForCheck())
                .setGenerateScriptsForDefault(plan.isGenerateScriptsForDefault());
    }
}
