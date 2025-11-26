package ru.akvine.iskra.services.facades;

import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.services.domain.connection.ConnectionModel;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.dto.ProcessPayload;

import java.util.Map;

public interface ScriptsFacade {
    void executeScript(ConnectionModel connection, String script);

    void disableSqlObjects(ProcessPayload payload);

    void clearTables(ProcessPayload payload);

    void enableSqlObjects(ProcessPayload payload);

    void generateScripts(PlanModel plan, Map<TableName, TableModel> selectedTables);
}
