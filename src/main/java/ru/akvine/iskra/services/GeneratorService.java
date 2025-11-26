package ru.akvine.iskra.services;

import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.dto.GenerateDataAction;

import java.util.Map;

public interface GeneratorService {
    void generate(GenerateDataAction action);

    void generateScripts(PlanModel plan, Map<TableName, TableModel> selectedTables);
}
