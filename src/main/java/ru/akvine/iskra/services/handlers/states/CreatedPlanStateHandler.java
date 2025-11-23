package ru.akvine.iskra.services.handlers.states;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.providers.StateHandlersProvider;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.handlers.AbstractStateHandler;

@Component
@Slf4j
public class CreatedPlanStateHandler extends AbstractStateHandler {
    protected CreatedPlanStateHandler(PlanService planService, StateHandlersProvider stateHandlersProvider) {
        super(planService, stateHandlersProvider);
    }

    @Override
    public void doHandle(PlanModel plan, boolean continueGeneration) {

    }

    @Override
    public PlanState getCurrentState() {
        return PlanState.CREATED;
    }

    @Override
    public PlanState toNextState() {
        return PlanState.STARTED;
    }

    @Override
    public PlanState toFailedStateIfError() {
        return PlanState.FAILED;
    }
}
