package ru.akvine.iskra.services.state_machine.handlers.states.process;

import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.state_machine.handlers.AbstractStateHandler;

import java.util.Map;

@Component
public class StartedStateHandler extends AbstractStateHandler {

    protected StartedStateHandler(PlanService planService) {
        super(planService);
    }

    @Override
    public void doHandle(PlanModel plan,
                         Map<TableName, TableModel> selectedTables,
                         boolean resume,
                         String processUuid) {
    }

    @Override
    public PlanState getCurrentState() {
        return PlanState.STARTED;
    }

    @Override
    public PlanState toNextState() {
        return PlanState.DISABLE_SQL_OBJECTS;
    }

    @Override
    public PlanState toFailedStateIfError() {
        return PlanState.STARTED_FAILED;
    }
}
