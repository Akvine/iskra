package ru.akvine.iskra.services.handlers.states;

import org.springframework.stereotype.Component;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.providers.StateHandlersProvider;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.handlers.AbstractStateHandler;

@Component
public class StartedStateHandler extends AbstractStateHandler {
    protected StartedStateHandler(PlanService planService, StateHandlersProvider stateHandlersProvider) {
        super(planService, stateHandlersProvider);
    }

    @Override
    public void doHandle(PlanModel plan, boolean continueGeneration) {

    }

    @Override
    public PlanState getCurrentState() {
        return PlanState.STARTED;
    }

    @Override
    public PlanState toNextState() {
        return PlanState.CLEAR_TABLES;
    }

    @Override
    public PlanState toFailedStateIfError() {
        return PlanState.STARTED_FAILED;
    }
}
