package ru.akvine.iskra.services;

import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.dto.plan.action.GenerateScriptsResult;

import java.util.Map;

public interface GeneratorFacade {
    String generateData(String planUuid,
                        Map<TableName, TableModel> selectedTables,
                        boolean resume);

    GenerateScriptsResult generateScripts(PlanModel plan,
                                          Map<TableName, TableModel> selectedTables);
}
