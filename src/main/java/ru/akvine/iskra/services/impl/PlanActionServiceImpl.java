package ru.akvine.iskra.services.impl;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.compozit.commons.utils.UUIDGenerator;
import ru.akvine.iskra.configs.async.executors.TaskExecutor;
import ru.akvine.iskra.exceptions.table.AnyTablesNotSelectedException;
import ru.akvine.iskra.services.GeneratorService;
import ru.akvine.iskra.services.PlanActionService;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.domain.table.TableService;
import ru.akvine.iskra.services.dto.plan.UpdatePlan;
import ru.akvine.iskra.services.dto.table.ListTables;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanActionServiceImpl implements PlanActionService {
    private final TableService tableService;
    private final PlanService planService;
    private final GeneratorService generatorService;

    private final TaskExecutor taskExecutor;

    @Override
    public String start(String planUuid) {
        Asserts.isNotNull(planUuid);
        planService.verifyExists(planUuid);

//        RelationsMatrixDto relationsMatrix = action.getRelationsMatrix();

        ListTables listTables = new ListTables()
                .setPlanUuid(planUuid)
                .setSelected(true);
        Map<TableName, TableModel> selectedTables = tableService
                .list(listTables)
                .stream().collect(Collectors.toMap(
                        table -> new TableName(table.getTableName()),
                        Function.identity()
                ));

        if (CollectionUtils.isEmpty(selectedTables)) {
            String message = "For plan = [" + planUuid + "] not selected any tables to generate data!";
            throw new AnyTablesNotSelectedException(message);
        }

//        List<TableName> tableNamesHasNoRelations = relationsMatrix.getRows().stream()
//                .filter(row -> !row.hasRelations())
//                .map(row -> new TableName(row.getTableName()))
//                .toList();

        List<TableName> tableNamesHasNoRelations = selectedTables.keySet().stream().toList();

        String processUuid = UUIDGenerator.uuid();
        UpdatePlan updateAction = new UpdatePlan()
                .setPlanUuid(planUuid)
                .setLastProcessUuid(processUuid);
        planService.update(updateAction);

        for (TableName tableName : tableNamesHasNoRelations) {
            try {
                CompletableFuture.runAsync(
                        () -> generatorService.generate(processUuid, selectedTables.get(tableName)),
                        taskExecutor.executor()
                );
            } catch (RejectedExecutionException exception) {
                log.info("Executor [{}] is full. Task was rejected");
            }

        }

        return processUuid;
    }

    @PreDestroy
    public void destroy() {
        taskExecutor.executor().shutdown();
    }
}
