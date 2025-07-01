package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.compozit.commons.visor.ConstraintType;
import ru.akvine.compozit.commons.visor.ScriptResultDto;
import ru.akvine.iskra.enums.ProcessState;
import ru.akvine.iskra.services.GeneratorCacheService;
import ru.akvine.iskra.services.GeneratorService;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.domain.table.configuration.TableConfigurationModel;
import ru.akvine.iskra.services.domain.table.configuration.TableConfigurationService;
import ru.akvine.iskra.services.domain.table.process.TableProcessModel;
import ru.akvine.iskra.services.domain.table.process.TableProcessService;
import ru.akvine.iskra.services.dto.GenerateDataAction;
import ru.akvine.iskra.services.domain.table.configuration.dto.UpdateTableConfiguration;
import ru.akvine.iskra.services.domain.table.process.dto.CreateTableProcess;
import ru.akvine.iskra.services.domain.table.process.dto.UpdateTableProcess;
import ru.akvine.iskra.services.integration.istochnik.IstochnikService;
import ru.akvine.iskra.services.integration.visor.VisorService;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestGeneratorServiceImpl implements GeneratorService {
    private final VisorService visorService;
    private final IstochnikService istochnikService;
    private final TableProcessService tableProcessService;
    private final GeneratorCacheService generatorCacheService;
    private final TableConfigurationService tableConfigurationService;

    @Value("${update.table.process.on.iteration}")
    private int updateIteration;

    @Override
    public boolean generate(GenerateDataAction action) {
        Asserts.isNotNull(action);

        String processUuid = action.getProcessUuid();
        TableModel table = action.getTable();
        TableConfigurationModel tableConfiguration = table.getConfiguration();

        TableProcessModel tableProcess;
        if (action.isResume()) {
            tableProcess = tableProcessService.get(processUuid, table.getTableName());
            UpdateTableProcess updateAction = new UpdateTableProcess()
                    .setProcessUuid(processUuid)
                    .setTableName(table.getTableName())
                    .setState(ProcessState.IN_PROGRESS);
            tableProcessService.update(updateAction);
        } else {
            CreateTableProcess createTableProcessAction = new CreateTableProcess()
                    .setPlanUuid(table.getPlan().getUuid())
                    .setUserUuid(action.getUserUuid())
                    .setProcessUuid(processUuid)
                    .setTableName(table.getTableName())
                    .setTotalRowsCount(table.getConfiguration().getRowsCount());
            tableProcess = tableProcessService.create(createTableProcessAction);
        }

        if (tableConfiguration.isDeleteDataBeforeStart() &&
                StringUtils.isNotBlank(tableConfiguration.getClearScript()) &&
                !action.isResume()) {
            log.info("Execute clear script for table = [{}]", table.getTableName());
            try {
                visorService.executeScripts(
                        Set.of(tableConfiguration.getClearScript()),
                        table.getPlan().getConnection());
            } catch (Exception exception) {
                UpdateTableProcess updateTableProcessAction = new UpdateTableProcess()
                        .setPid(tableProcess.getPid())
                        .setAddSuccessRowsCount(null)
                        .setErrorMessage(exception.getMessage())
                        .setState(ProcessState.FAILED);
                tableProcessService.update(updateTableProcessAction);
                return false;
            }
        }

        String pid = tableProcess.getPid();
        generateDataInternal(pid, table, tableProcess);
        return true;
    }

    @Override
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
                selectedTables.keySet().stream()
                        .map(TableName::getName).toList(),
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

    private void generateDataInternal(String pid, TableModel tableModel, TableProcessModel tableProcess) {
        UpdateTableProcess updateTableProcessAction = new UpdateTableProcess()
                .setPid(pid);
        TableConfigurationModel configuration = tableModel.getConfiguration();

        int processedRowsCount = (int) tableProcess.getSuccessRowsCount();

        int processedRowsCountBeforeUpdate = 0;
        int iterationCounter = 1;
        try {
            while (processedRowsCount < configuration.getRowsCount()) {
                if (isStopped(tableModel.getPlan().getUuid())) {
                    updateTableProcessAction.setAddSuccessRowsCount((long) processedRowsCountBeforeUpdate);
                    updateTableProcessAction.setState(ProcessState.STOPPED);
                    tableProcessService.update(updateTableProcessAction);
                    Thread.currentThread().interrupt();
                    return;
                }

                log.info("Sending batch count = [{}] for [{}]. Batch size = [{}]",
                        iterationCounter,
                        tableModel.getTableName(),
                        configuration.getBatchSize());
                byte[] table = istochnikService.generatedData(processedRowsCount, tableModel);
                if (isStopped(tableModel.getPlan().getUuid())) {
                    updateTableProcessAction.setAddSuccessRowsCount((long) processedRowsCountBeforeUpdate);
                    updateTableProcessAction.setState(ProcessState.STOPPED);
                    tableProcessService.update(updateTableProcessAction);
                    Thread.currentThread().interrupt();
                    return;
                }

                visorService.sendFile(tableModel, table);
                processedRowsCountBeforeUpdate += configuration.getBatchSize();

                if (iterationCounter % updateIteration == 0) {
                    log.info("Update table process with pid = [{}] and table name = [{}]. Processed rows count = [{}]",
                            pid,
                            tableModel.getTableName(),
                            processedRowsCount);
                    updateTableProcessAction.setAddSuccessRowsCount((long) processedRowsCountBeforeUpdate);
                    tableProcessService.update(updateTableProcessAction);
                    processedRowsCountBeforeUpdate = 0;
                }

                processedRowsCount += configuration.getBatchSize();
                iterationCounter++;
            }

            log.info("Table with pid = [{}] and name = [{}] was successfully filled in!", pid, tableModel.getTableName());
            updateTableProcessAction.setCompletedDate(new Date());
            updateTableProcessAction.setState(ProcessState.SUCCESS);
            updateTableProcessAction.setAddSuccessRowsCount(null);
        } catch (Exception exception) {
            updateTableProcessAction
                    .setAddSuccessRowsCount(null)
                    .setErrorMessage(exception.getMessage())
                    .setState(ProcessState.FAILED);
        } finally {
            tableProcessService.update(updateTableProcessAction);
        }
    }

    private boolean isStopped(String planUuid) {
        return generatorCacheService.isStopped(planUuid);
    }
}
