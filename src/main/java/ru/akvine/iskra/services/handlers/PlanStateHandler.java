package ru.akvine.iskra.services.handlers;

import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.table.TableModel;

import java.util.Map;

public interface PlanStateHandler {
    void process(PlanModel planModel,
                 Map<TableName, TableModel> selectedTables,
                 boolean resume,
                 String processUuid);

    PlanState getCurrentState();

    PlanState toNextState();

    PlanState toFailedStateIfError();
}
