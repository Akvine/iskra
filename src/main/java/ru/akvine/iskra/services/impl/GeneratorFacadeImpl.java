package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.configs.async.executors.TaskExecutor;
import ru.akvine.iskra.services.GeneratorFacade;
import ru.akvine.iskra.services.GeneratorService;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.plan.dto.action.GenerateScriptsResult;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.domain.table.configuration.TableConfigurationModel;
import ru.akvine.iskra.services.domain.table.configuration.TableConfigurationService;
import ru.akvine.iskra.services.dto.GenerateDataAction;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeneratorFacadeImpl implements GeneratorFacade {
    private final TaskExecutor taskExecutor;
    private final GeneratorService generatorService;
    private final TableConfigurationService tableConfigurationService;

    @Override
    public String generateData(String processUuid,
                               String userUuid,
                               Map<TableName, TableModel> selectedTables,
                               boolean resume) {
        Asserts.isNotNull(selectedTables);

        Set<TableName> tableNames = selectedTables.keySet();
        for (TableName tableName : tableNames) {
            try {
                CompletableFuture.runAsync(
                        () -> {
                            GenerateDataAction generateDataAction = new GenerateDataAction()
                                    .setProcessUuid(processUuid)
                                    .setTable(selectedTables.get(tableName))
                                    .setResume(resume);
                            generatorService.generate(generateDataAction);
                        },
                        taskExecutor.executor()
                );
            } catch (RejectedExecutionException exception) {
                log.info("Executor is full. Task was rejected");
            }
        }

        return processUuid;
    }

    @Override
    public GenerateScriptsResult generateScripts(PlanModel plan,
                                                 Map<TableName, TableModel> selectedTables) {
        Asserts.isNotNull(plan);
        Asserts.isNotEmpty(selectedTables);

        generatorService.generateScripts(plan, selectedTables);
        GenerateScriptsResult result = new GenerateScriptsResult();
        List<TableConfigurationModel> configs = tableConfigurationService.list(
                plan.getUuid(), selectedTables.keySet().stream().map(TableName::getName).toList()
        );
        configs.forEach(config -> {
            result.getDropScripts().addAll(config.getDropScripts());
            result.getCreateScripts().addAll(config.getCreateScripts());
            result.getClearScripts().add(config.getClearScript());
        });

        return result;
    }
}
