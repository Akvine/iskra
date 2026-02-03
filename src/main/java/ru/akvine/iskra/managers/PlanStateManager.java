package ru.akvine.iskra.managers;

import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.table.TableModel;

import java.util.Map;

public interface PlanStateManager {
    void manage(PlanModel planModel,
                Map<TableName, TableModel> selectedTables,
                boolean resume,
                String processUuid);
}
