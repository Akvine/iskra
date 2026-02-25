package ru.akvine.iskra.managers;

import java.util.Map;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.table.TableModel;

public interface PlanStateManager {
    void manage(
            PlanModel planModel,
            Map<TableName, TableModel> selectedTables,
            boolean resume,
            String planProcessUuid,
            String userUuid);
}
