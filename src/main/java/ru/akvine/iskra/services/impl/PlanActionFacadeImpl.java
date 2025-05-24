package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.akvine.compozit.commons.ConnectionDto;
import ru.akvine.compozit.commons.RelationsMatrixDto;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.compozit.commons.utils.UUIDGenerator;
import ru.akvine.iskra.enums.ProcessState;
import ru.akvine.iskra.events.GenerateDataEvent;
import ru.akvine.iskra.exceptions.IntegrationException;
import ru.akvine.iskra.repositories.PlanRepository;
import ru.akvine.iskra.repositories.entities.PlanEntity;
import ru.akvine.iskra.services.PlanActionFacade;
import ru.akvine.iskra.services.PlanService;
import ru.akvine.iskra.services.TableProcessService;
import ru.akvine.iskra.services.TableService;
import ru.akvine.iskra.services.domain.TableModel;
import ru.akvine.iskra.services.domain.TableProcessModel;
import ru.akvine.iskra.services.domain.configuration.TableConfigurationModel;
import ru.akvine.iskra.services.dto.GenerateDataAction;
import ru.akvine.iskra.services.dto.process.CreateTableProcess;
import ru.akvine.iskra.services.dto.process.UpdateTableProcess;
import ru.akvine.iskra.services.dto.table.ListTables;
import ru.akvine.iskra.services.integration.istochnik.IstochnikService;
import ru.akvine.iskra.services.integration.visor.VisorService;

import java.util.Date;
import java.util.List;
import java.util.Map;
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
    private final PlanRepository planRepository;

    @Value("${update.table.process.on.iteration}")
    private int updateIteration;

    // TODO: из-за циклической зависимости TableProcessService от PlanService пришлось сделать обработку событий
    @EventListener
    @Async
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
        PlanEntity plan = planService.verifyExists(action.getPlanUuid());
        plan.setLastProcessUuid(processUuid);
        planRepository.save(plan);

        log.info("Start generation data process with uuid = {}", processUuid);

        for (TableName tableName : tableNamesHasNoRelations) {
            generateData(processUuid, selectedTables.get(tableName));
        }

        return processUuid;
    }

    @Override
    public TableProcessModel generateData(String processUuid, TableModel table) {
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
}
