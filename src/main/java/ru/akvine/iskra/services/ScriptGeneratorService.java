package ru.akvine.iskra.services;

import java.util.Map;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.plan.dto.action.GenerateScriptsResult;
import ru.akvine.iskra.services.domain.table.TableModel;

public interface ScriptGeneratorService {
    GenerateScriptsResult generateScripts(PlanModel plan, Map<TableName, TableModel> selectedTables);
}
