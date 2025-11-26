package ru.akvine.iskra.services.handlers.states.failed;

import org.springframework.stereotype.Component;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.providers.StateHandlersProvider;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.handlers.AbstractStateHandler;

@Component
public class StartedFailedStateHandler extends AbstractStateHandler {
    protected StartedFailedStateHandler(PlanService planService, StateHandlersProvider stateHandlersProvider) {
        super(planService, stateHandlersProvider);
    }

    @Override
    public PlanState getCurrentState() {
        return PlanState.STARTED_FAILED;
    }

    @Override
    public PlanState toNextState() {
        return PlanState.STARTED;
    }

    @Override
    public PlanState toFailedStateIfError() {
        return PlanState.STARTED_FAILED;
    }
}
