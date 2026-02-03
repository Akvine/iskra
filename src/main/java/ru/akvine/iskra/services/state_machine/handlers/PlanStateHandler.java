package ru.akvine.iskra.services.state_machine.handlers;

import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.table.TableModel;

import java.util.Map;

public interface PlanStateHandler {
    PlanModel process(PlanModel planModel,
                 Map<TableName, TableModel> selectedTables,
                 boolean resume,
                 String processUuid);

    PlanState getCurrentState();

    PlanState toNextState();

    PlanState toFailedStateIfError();
}
