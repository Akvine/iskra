package ru.akvine.iskra.services.state_machine.handlers.states.failed;

import org.springframework.stereotype.Component;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.state_machine.handlers.AbstractStateHandler;

@Component
public class ClearTablesFailedStateHandler extends AbstractStateHandler {
    protected ClearTablesFailedStateHandler(PlanService planService) {
        super(planService);
    }

    @Override
    public PlanState getCurrentState() {
        return PlanState.CLEAR_TABLES_FAILED;
    }

    @Override
    public PlanState toNextState() {
        return PlanState.CLEAR_TABLES;
    }

    @Override
    public PlanState toFailedStateIfError() {
        return PlanState.CLEAR_TABLES_FAILED;
    }
}
