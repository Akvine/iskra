package ru.akvine.iskra.rest.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.components.SecurityManager;
import ru.akvine.iskra.rest.dto.plan.actions.StartPlanRequest;
import ru.akvine.iskra.rest.dto.plan.actions.StartPlanResponse;
import ru.akvine.iskra.services.dto.plan.action.StartAction;

@Component
@RequiredArgsConstructor
public class PlanActionsMapper {
    private final SecurityManager securityManager;

    public StartAction mapToStartAction(StartPlanRequest request) {
        Asserts.isNotNull(request);
        return new StartAction()
                .setUserUuid(securityManager.getCurrentUser().getUuid())
                .setPlanUuid(request.getPlanUuid())
                .setResume(false);
    }

    public StartPlanResponse convertToStartPlanResponse(String processUuid) {
        return new StartPlanResponse().setProcessUuid(processUuid);
    }
}
