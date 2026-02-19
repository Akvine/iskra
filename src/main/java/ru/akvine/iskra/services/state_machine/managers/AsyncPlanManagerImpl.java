package ru.akvine.iskra.services.state_machine.managers;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.compozit.commons.utils.UUIDGenerator;
import ru.akvine.iskra.components.SecurityManager;
import ru.akvine.iskra.configs.async.executors.TaskExecutor;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.exceptions.plan.PlanAlreadyStartedException;
import ru.akvine.iskra.exceptions.plan.PlanStartingException;
import ru.akvine.iskra.exceptions.table.AnyTablesNotSelectedException;
import ru.akvine.iskra.exceptions.table.configuration.TableConfigurationNotFoundException;
import ru.akvine.iskra.managers.PlanStateManager;
import ru.akvine.iskra.services.GeneratorCacheService;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.domain.plan.dto.UpdatePlan;
import ru.akvine.iskra.services.domain.process.PlanProcessService;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.domain.table.TableService;
import ru.akvine.iskra.services.domain.table.dto.ListTables;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsyncPlanManagerImpl implements PlanManager {
    private final TaskExecutor taskExecutor;
    private final PlanStateManager planStateManager;
    private final SecurityManager securityManager;
    private final GeneratorCacheService generatorCacheService;
    private final TableService tableService;
    private final PlanService planService;
    private final PlanProcessService planProcessService;

    @Override
    public String start(PlanModel plan, boolean resume) {
        Asserts.isNotNull(plan);

        String userUuid = securityManager.getCurrentUser().getUuid();
        String planUuid = plan.getUuid();

        if (plan.getPlanState() == PlanState.IN_PROGRESS) {
            String message = String.format("Plan with uuid = [%s] and name = [%s] has already started", plan.getUuid(), plan.getName());
            throw new PlanAlreadyStartedException(message);
        }

        ListTables listTables = new ListTables()
                .setPlanUuid(planUuid)
                .setUserUuid(userUuid)
                .setSelected(true)
                .setResume(resume);
        Map<TableName, TableModel> selectedTables = tableService
                .list(listTables)
                .stream().collect(Collectors.toMap(
                        table -> new TableName(table.getTableName()),
                        Function.identity()
                ));

        String processUuid;
        if (resume) {
            processUuid = planProcessService.getLastStoppedOrFailed(planUuid).getUuid();
        } else {
            // Валидируем вабранные таблицы перед запуском
            if (CollectionUtils.isEmpty(selectedTables)) {
                String message = "For plan = [" + plan.getName() + "] not selected any tables to generate data!";
                throw new AnyTablesNotSelectedException(message);
            }

            selectedTables.values().forEach(table -> {
                if (table.getConfiguration() == null) {
                    String errorMessage = String.format("Table with name = [%s] has no configuration!", table.getTableName());
                    throw new TableConfigurationNotFoundException(errorMessage);
                }
            });

             processUuid = UUIDGenerator.uuid();
        }

        // Асинхронно запускаем выполнение генерации для плана
        try {
            CompletableFuture.runAsync(
                            () -> planStateManager.manage(plan, selectedTables, resume, processUuid),
                            taskExecutor.executor());
        } catch (RejectedExecutionException exception) {
            String errorMessage = String.format(
                    "Error while start plan with uuid = [%s] and name = [%s]",
                    plan.getUuid(), plan.getName()
            );
            throw new PlanStartingException(errorMessage, exception);
        }

        return processUuid;
    }

    @Override
    public boolean stop(PlanModel plan) {
        Asserts.isNotNull(plan);

        if (plan.getPlanState() == PlanState.IN_PROGRESS) {
            UpdatePlan action = new UpdatePlan()
                    .setPlanUuid(plan.getUuid())
                    .setUserUuid(plan.getUser().getUuid())
                    .setPlanState(PlanState.STOPPED);
            planService.update(action);
            return generatorCacheService.stop(plan.getUuid());
        }

        String errorMessage = String.format(
                "Plan with uuid = [%s] and name = [%s] has illegal state = [%s] for stopping!",
                plan.getUuid(), plan.getName(), plan.getPlanState()
        );
        throw new IllegalStateException(errorMessage);
    }

    @PreDestroy
    public void destroy() {
        if (taskExecutor.executor() != null) {
            taskExecutor.executor().shutdown();
        }
    }
}
