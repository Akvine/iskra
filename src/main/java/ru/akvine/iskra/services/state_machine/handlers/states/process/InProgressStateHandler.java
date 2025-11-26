package ru.akvine.iskra.services.state_machine.handlers.states.process;

import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.providers.StateHandlersProvider;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.facades.DataGeneratorFacade;
import ru.akvine.iskra.services.state_machine.handlers.AbstractStateHandler;

import java.util.Map;

@Component
public class InProgressStateHandler extends AbstractStateHandler {
    private final DataGeneratorFacade dataGeneratorFacade;

    protected InProgressStateHandler(PlanService planService, StateHandlersProvider stateHandlersProvider, DataGeneratorFacade dataGeneratorFacade) {
        super(planService, stateHandlersProvider);
        this.dataGeneratorFacade = dataGeneratorFacade;
    }

    @Override
    public void doHandle(PlanModel plan,
                         Map<TableName, TableModel> selectedTables,
                         boolean resume,
                         String processUuid) {
        selectedTables.forEach((tableName, table) ->
                dataGeneratorFacade.generate(processUuid, table, resume));
    }

    @Override
    public PlanState getCurrentState() {
        return PlanState.IN_PROGRESS;
    }

    @Override
    public PlanState toNextState() {
        return PlanState.ENABLE_SQL_OBJECTS;
    }

    @Override
    public PlanState toFailedStateIfError() {
        return PlanState.FAILED;
    }
}
