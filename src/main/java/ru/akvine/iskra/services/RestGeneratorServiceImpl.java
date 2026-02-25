package ru.akvine.iskra.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.compozit.commons.visor.ConstraintType;
import ru.akvine.compozit.commons.visor.ScriptResultDto;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.domain.table.configuration.TableConfigurationService;
import ru.akvine.iskra.services.domain.table.configuration.dto.UpdateTableConfiguration;
import ru.akvine.iskra.services.integration.visor.VisorService;

@Service
@RequiredArgsConstructor
public class RestGeneratorServiceImpl {
    private final VisorService visorService;
    private final TableConfigurationService tableConfigurationService;

    public void generateScripts(PlanModel plan, Map<TableName, TableModel> selectedTables) {
        List<String> constraintsToGenerateScripts = new ArrayList<>();
        constraintsToGenerateScripts.add(ConstraintType.FOREIGN_KEY.getName());
        if (plan.isGenerateScriptsForCheck()) {
            constraintsToGenerateScripts.add(ConstraintType.CHECK.getName());
        }
        if (plan.isGenerateScriptsForIndex()) {
            constraintsToGenerateScripts.add(ConstraintType.INDEX.getName());
        }
        if (plan.isGenerateScriptsForDefault()) {
            constraintsToGenerateScripts.add(ConstraintType.DEFAULT.getName());
        }
        if (plan.isGenerateScriptsForTrigger()) {
            constraintsToGenerateScripts.add(ConstraintType.TRIGGER.getName());
        }
        if (plan.isGenerateScriptsForUnique()) {
            constraintsToGenerateScripts.add(ConstraintType.UNIQUE.getName());
        }
        if (plan.isGenerateScriptsForNotNull()) {
            constraintsToGenerateScripts.add(ConstraintType.NOT_NULL.getName());
        }
        if (plan.isGenerateScriptsForPrimaryKey()) {
            constraintsToGenerateScripts.add(ConstraintType.PRIMARY_KEY.getName());
        }

        Map<String, ScriptResultDto> generatedScripts = visorService.generateScriptsForTables(
                selectedTables.keySet().stream().map(TableName::getName).toList(),
                constraintsToGenerateScripts,
                plan.getConnection());

        for (Map.Entry<String, ScriptResultDto> pair : generatedScripts.entrySet()) {
            // TODO: N+1 при обращаении в базу в цикле для метода tableConfigurationService.update()
            UpdateTableConfiguration updateAction = new UpdateTableConfiguration()
                    .setPlanUuid(plan.getUuid())
                    .setTableName(pair.getKey())
                    .setDropScripts(pair.getValue().getDropScripts())
                    .setCreateScripts(pair.getValue().getCreateScripts());

            tableConfigurationService.update(updateAction);
        }
    }
}
