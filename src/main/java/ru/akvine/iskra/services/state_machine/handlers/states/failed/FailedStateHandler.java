package ru.akvine.iskra.services.state_machine.handlers.states.failed;

import org.springframework.stereotype.Component;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.state_machine.handlers.AbstractStateHandler;

@Component
public class FailedStateHandler extends AbstractStateHandler {
    protected FailedStateHandler(PlanService planService) {
        super(planService);
    }

    @Override
    public PlanState getCurrentState() {
        return PlanState.FAILED;
    }

    @Override
    public PlanState toNextState() {
        return PlanState.IN_PROGRESS;
    }

    @Override
    public PlanState toFailedStateIfError() {
        return PlanState.FAILED;
    }
}
