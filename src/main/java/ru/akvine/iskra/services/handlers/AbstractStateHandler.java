package ru.akvine.iskra.services.handlers;

import lombok.extern.slf4j.Slf4j;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.providers.StateHandlersProvider;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.domain.plan.dto.UpdatePlan;

@Slf4j
public abstract class AbstractStateHandler implements PlanStateHandler {
    private final PlanService planService;
    private final StateHandlersProvider stateHandlersProvider;

    protected AbstractStateHandler(PlanService planService,
                                   StateHandlersProvider stateHandlersProvider) {
        this.planService = planService;
        this.stateHandlersProvider = stateHandlersProvider;
    }

    @Override
    public final void process(PlanModel plan, boolean continueProcess) {
        log.info("Handle plan: uuid = [{}] and name = [{}]. Moving from [{}] state to [{}]...",
                plan.getUuid(), plan.getName(), getCurrentState(), toNextState());
        try {
            doHandle(plan, continueProcess);
            updateState(plan, toNextState());
            stateHandlersProvider.getByState(toNextState()).process(plan, continueProcess);
        } catch (RuntimeException exception) {
            updateState(plan, toFailedStateIfError());
            doHandleException(exception);
        }
    }

    public void updateState(PlanModel plan, PlanState nextState) {
        UpdatePlan action = new UpdatePlan()
                .setPlanUuid(plan.getUuid())
                .setPlanState(nextState)
                .setUserUuid(plan.getUser().getUuid());
        planService.update(action);
    }

    public abstract void doHandle(PlanModel plan, boolean continueGeneration);

    protected <T extends RuntimeException> void doHandleException(T exception) throws T {
        throw exception;
    }
}
