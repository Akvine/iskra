package ru.akvine.iskra.services.state_machine.handlers.states.failed;

import org.springframework.stereotype.Component;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.state_machine.handlers.AbstractStateHandler;

@Component
public class EnableSqlObjectsFailedStateHandler extends AbstractStateHandler {
    protected EnableSqlObjectsFailedStateHandler(PlanService planService) {
        super(planService);
    }

    @Override
    public PlanState getCurrentState() {
        return PlanState.ENABLE_SQL_OBJECTS_FAILED;
    }

    @Override
    public PlanState toNextState() {
        return PlanState.ENABLE_SQL_OBJECTS;
    }

    @Override
    public PlanState toFailedStateIfError() {
        return PlanState.ENABLE_SQL_OBJECTS_FAILED;
    }
}
