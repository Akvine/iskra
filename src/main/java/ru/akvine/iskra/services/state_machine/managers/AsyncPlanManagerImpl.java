package ru.akvine.iskra.services.state_machine.managers;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.compozit.commons.utils.UUIDGenerator;
import ru.akvine.iskra.components.SecurityManager;
import ru.akvine.iskra.configs.async.executors.TaskExecutor;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.exceptions.plan.PlanAlreadyStartedException;
import ru.akvine.iskra.exceptions.plan.PlanStartingException;
import ru.akvine.iskra.services.GeneratorCacheService;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.domain.plan.dto.UpdatePlan;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.domain.table.TableService;
import ru.akvine.iskra.services.domain.table.dto.ListTables;
import ru.akvine.iskra.services.state_machine.handlers.PlanStateHandler;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsyncPlanManagerImpl implements PlanManager {
    private final TaskExecutor taskExecutor;
    private final PlanStateHandler planStateHandler;
    private final SecurityManager securityManager;
    private final GeneratorCacheService generatorCacheService;
    private final TableService tableService;
    private final PlanService planService;

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
                .setSelected(true);
        Map<TableName, TableModel> selectedTables = tableService
                .list(listTables)
                .stream().collect(Collectors.toMap(
                        table -> new TableName(table.getTableName()),
                        Function.identity()
                ));

        String processUuid;

        if (resume) {
            processUuid = plan.getLastProcessUuid();
            generatorCacheService.remove(planUuid);
        } else {
            processUuid = UUIDGenerator.uuid();
            UpdatePlan updateAction = new UpdatePlan()
                    .setPlanUuid(planUuid)
                    .setUserUuid(userUuid)
                    .setLastProcessUuid(processUuid);
            planService.update(updateAction);
        }

        // Асинхронно запускаем выполнение таски
        try {
            CompletableFuture.runAsync(() -> planStateHandler.process(plan, selectedTables, resume, processUuid), taskExecutor.executor());
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
