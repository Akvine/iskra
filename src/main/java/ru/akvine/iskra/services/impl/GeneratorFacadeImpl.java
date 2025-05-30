package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.compozit.commons.utils.UUIDGenerator;
import ru.akvine.iskra.configs.async.executors.TaskExecutor;
import ru.akvine.iskra.services.GeneratorFacade;
import ru.akvine.iskra.services.GeneratorService;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.dto.plan.UpdatePlan;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeneratorFacadeImpl implements GeneratorFacade {
    private final GeneratorService generatorService;
    private final PlanService planService;
    private final TaskExecutor taskExecutor;

    @Override
    public String generate(String planUuid, Map<TableName, TableModel> selectedTables) {
        Asserts.isNotNull(selectedTables);

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
}
