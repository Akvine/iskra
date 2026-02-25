package ru.akvine.iskra.services.facades;

import java.util.Map;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.dto.ProcessPayload;

public interface ScriptsFacade {
    void disableSqlObjects(ProcessPayload payload);

    void clearTables(ProcessPayload payload);

    void enableSqlObjects(ProcessPayload payload);

    void generateScripts(PlanModel plan, Map<TableName, TableModel> selectedTables);
}
