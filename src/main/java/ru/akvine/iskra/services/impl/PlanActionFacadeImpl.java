package ru.akvine.iskra.services.impl;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.akvine.compozit.commons.ConnectionDto;
import ru.akvine.compozit.commons.RelationsMatrixDto;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.compozit.commons.utils.UUIDGenerator;
import ru.akvine.iskra.configs.async.executors.ParallelGenerationExecutor;
import ru.akvine.iskra.enums.ProcessState;
import ru.akvine.iskra.events.GenerateDataEvent;
import ru.akvine.iskra.exceptions.IntegrationException;
import ru.akvine.iskra.exceptions.table.configuration.TableConfigurationNotFoundException;
import ru.akvine.iskra.services.PlanActionFacade;
import ru.akvine.iskra.services.PlanService;
import ru.akvine.iskra.services.TableProcessService;
import ru.akvine.iskra.services.TableService;
import ru.akvine.iskra.services.domain.TableModel;
import ru.akvine.iskra.services.domain.TableProcessModel;
import ru.akvine.iskra.services.domain.configuration.TableConfigurationModel;
import ru.akvine.iskra.services.dto.GenerateDataAction;
import ru.akvine.iskra.services.dto.plan.UpdatePlan;
import ru.akvine.iskra.services.dto.process.CreateTableProcess;
import ru.akvine.iskra.services.dto.process.UpdateTableProcess;
import ru.akvine.iskra.services.dto.table.ListTables;
import ru.akvine.iskra.services.integration.istochnik.IstochnikService;
import ru.akvine.iskra.services.integration.visor.VisorService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanActionFacadeImpl implements PlanActionFacade {
    private final VisorService visorService;
    private final IstochnikService istochnikService;
    private final TableProcessService tableProcessService;
    private final TableService tableService;
    private final PlanService planService;

    private final ParallelGenerationExecutor executor;

    @Value("${update.table.process.on.iteration}")
    private int updateIteration;
    @Value("${parallel.execution.enabled}")
    private boolean parallelExecutionEnabled;

    @PostConstruct
    public void init() {
        if (parallelExecutionEnabled) {
            log.info("Parallel generation functionality is enabled");
        }
    }

    @EventListener
    public void handleEvent(GenerateDataEvent event) {
        generateData(event.getAction(), event.getConnection());
    }

    @Override
    public String generateData(GenerateDataAction action, ConnectionDto connection) {
        RelationsMatrixDto relationsMatrix = action.getRelationsMatrix();

        ListTables listTables = new ListTables()
                .setPlanUuid(action.getPlanUuid())
                .setSelected(true);
        Map<TableName, TableModel> selectedTables = tableService
                .list(listTables)
                .stream().collect(Collectors.toMap(
                        table -> new TableName(table.getTableName()),
                        Function.identity()
                ));

        if (CollectionUtils.isEmpty(selectedTables)) {
            return null;
        }

        List<TableName> tableNamesHasNoRelations = relationsMatrix.getRows().stream()
                .filter(row -> !row.hasRelations())
                .map(row -> new TableName(row.getTableName()))
                .toList();

        // TODO: создать отдельный метод сервисного класса для обновления сущности PlanEntity
        String processUuid = UUIDGenerator.uuid();
        UpdatePlan updateAction = new UpdatePlan()
                .setPlanUuid(action.getPlanUuid())
                .setLastProcessUuid(processUuid);
        planService.update(updateAction);

        log.info("Start generation data process with uuid = {}", processUuid);
        if (parallelExecutionEnabled) {
            try {
                List<CompletableFuture<Void>> futures = tableNamesHasNoRelations.stream()
                        .map(tableName -> CompletableFuture.runAsync(
                                () -> generateData(processUuid, selectedTables.get(tableName)),
                                executor.executor()))
                        .toList();
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            } catch (RejectedExecutionException exception) {
                log.info("Executor [{}] is full. Task was rejected: ",
                        executor.executor().toString(),
                        exception);
            }
        } else {
            for (TableName tableName : tableNamesHasNoRelations) {
                generateData(processUuid, selectedTables.get(tableName));
            }
        }

        return processUuid;
    }

    @Override
    public TableProcessModel generateData(String processUuid, TableModel table) {
        TableConfigurationModel tableConfiguration = table.getConfiguration();
        if (tableConfiguration == null) {
            String errorMessage = String.format("Table with name = [%s] has no configuration!", table.getTableName());
            throw new TableConfigurationNotFoundException(errorMessage);
        }

        if (tableConfiguration.isDeleteDataBeforeStart() &&
                StringUtils.isNotBlank(tableConfiguration.getClearScript())) {
            log.info("Execute clear script for table = [{}]", table.getTableName());
            visorService.executeScripts(
                    Set.of(tableConfiguration.getClearScript()),
                    table.getPlan().getConnection()
            );
        }

        CreateTableProcess createTableProcessAction = new CreateTableProcess()
                .setPlanUuid(table.getPlan().getUuid())
                .setProcessUuid(processUuid)
                .setTableName(table.getTableName())
                .setTotalRowsCount(table.getConfiguration().getRowsCount());
        TableProcessModel tableProcess = tableProcessService.create(createTableProcessAction);
        String pid = tableProcess.getPid();
        generateDataInternal(pid, table);
        return tableProcess;
    }

    private void generateDataInternal(String pid, TableModel tableModel) {
        UpdateTableProcess updateTableProcessAction = new UpdateTableProcess()
                .setPid(pid);
        TableConfigurationModel configuration = tableModel.getConfiguration();
        int processedRowsCount = 0;

        int processedRowsCountBeforeUpdate = 0;
        int iterationCounter = 1;
        try {
            while (processedRowsCount < configuration.getRowsCount()) {
                log.info("Sending batch count = [{}] for [{}]. Batch size = [{}]",
                        iterationCounter,
                        tableModel.getTableName(),
                        configuration.getBatchSize());
                byte[] table = istochnikService.generatedData(processedRowsCount, tableModel);
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
        } catch (IntegrationException exception) {
            updateTableProcessAction
                    .setAddSuccessRowsCount(null)
                    .setErrorMessage(exception.getMessage())
                    .setState(ProcessState.FAILED);
        } finally {
            tableProcessService.update(updateTableProcessAction);
        }
    }

    @PreDestroy
    public void destroy() {
        executor.executor().shutdown();
    }
}
