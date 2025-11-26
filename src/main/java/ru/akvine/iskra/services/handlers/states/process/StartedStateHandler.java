package ru.akvine.iskra.services.handlers.states.process;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.exceptions.table.AnyTablesNotSelectedException;
import ru.akvine.iskra.exceptions.table.configuration.TableConfigurationNotFoundException;
import ru.akvine.iskra.providers.StateHandlersProvider;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.handlers.AbstractStateHandler;

import java.util.Map;

@Component
public class StartedStateHandler extends AbstractStateHandler {
    protected StartedStateHandler(PlanService planService, StateHandlersProvider stateHandlersProvider) {
        super(planService, stateHandlersProvider);
    }

    @Override
    public void doHandle(PlanModel plan, Map<TableName,
            TableModel> selectedTables,
                         boolean resume,
                         String processUuid) {
        if (CollectionUtils.isEmpty(selectedTables)) {
            String message = "For plan = [" + plan.getName() + "] not selected any tables to generate data!";
            throw new AnyTablesNotSelectedException(message);
        }

        selectedTables.values().forEach(table -> {
            if (table.getConfiguration() == null) {
                String errorMessage = String.format("Table with name = [%s] has no configuration!", table.getTableName());
                throw new TableConfigurationNotFoundException(errorMessage);
            }
        });
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
