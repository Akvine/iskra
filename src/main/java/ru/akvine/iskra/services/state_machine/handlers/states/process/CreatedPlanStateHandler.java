package ru.akvine.iskra.services.state_machine.handlers.states.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.state_machine.handlers.AbstractStateHandler;

@Component
@Slf4j
@Primary
public class CreatedPlanStateHandler extends AbstractStateHandler {
    protected CreatedPlanStateHandler(PlanService planService) {
        super(planService);
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
        return null;
    }
}
