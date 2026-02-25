package ru.akvine.iskra.services.impl;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.services.RestGeneratorServiceImpl;
import ru.akvine.iskra.services.ScriptGeneratorService;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.plan.dto.action.GenerateScriptsResult;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.domain.table.configuration.TableConfigurationModel;
import ru.akvine.iskra.services.domain.table.configuration.TableConfigurationService;

@Service
@RequiredArgsConstructor
public class ScriptGeneratorServiceImpl implements ScriptGeneratorService {
    private final RestGeneratorServiceImpl restGeneratorService;
    private final TableConfigurationService tableConfigurationService;

    @Override
    public GenerateScriptsResult generateScripts(PlanModel plan, Map<TableName, TableModel> selectedTables) {
        Asserts.isNotNull(plan);
        Asserts.isNotEmpty(selectedTables);

        restGeneratorService.generateScripts(plan, selectedTables);
        GenerateScriptsResult result = new GenerateScriptsResult();
        List<TableConfigurationModel> configs = tableConfigurationService.list(
                plan.getUuid(),
                selectedTables.keySet().stream().map(TableName::getName).toList());
        configs.forEach(config -> {
            result.getDropScripts().addAll(config.getDropScripts());
            result.getCreateScripts().addAll(config.getCreateScripts());
            result.getClearScripts().add(config.getClearScript());
        });

        return result;
    }
}
