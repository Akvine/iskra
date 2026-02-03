package ru.akvine.iskra.services.state_machine.handlers;

import lombok.extern.slf4j.Slf4j;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.domain.plan.dto.UpdatePlan;
import ru.akvine.iskra.services.domain.table.TableModel;

import java.util.Map;

@Slf4j
public abstract class AbstractStateHandler implements PlanStateHandler {
    private final PlanService planService;

    protected AbstractStateHandler(PlanService planService) {
        this.planService = planService;
    }

    @Override
    public final PlanModel process(PlanModel plan,
                              Map<TableName, TableModel> selectedTables,
                              boolean resume,
                              String processUuid) {
        log.debug("Handle plan: uuid = [{}] and name = [{}]. Moving from [{}] state to [{}]...",
                plan.getUuid(), plan.getName(), getCurrentState(), toNextState());
        try {
            doHandle(plan, selectedTables, resume, processUuid);
            updateState(plan, toNextState());
        } catch (RuntimeException exception) {
            updateState(plan, toFailedStateIfError());
            doHandleException(exception);
        }

        return plan;
    }

    public void updateState(PlanModel plan, PlanState nextState) {
        UpdatePlan action = new UpdatePlan()
                .setPlanUuid(plan.getUuid())
                .setPlanState(nextState)
                .setUserUuid(plan.getUser().getUuid());
        planService.update(action);
    }

    public void doHandle(PlanModel plan, Map<TableName, TableModel> selectedTables,
                                  boolean resume,
                                  String processUuid) {

    }

    protected <T extends RuntimeException> void doHandleException(T exception) throws T {
        throw exception;
    }
}
