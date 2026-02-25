package ru.akvine.iskra.services.facades.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.compozit.commons.visor.ConstraintType;
import ru.akvine.compozit.commons.visor.ScriptResultDto;
import ru.akvine.iskra.enums.ProcessState;
import ru.akvine.iskra.enums.SqlScriptType;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.statistics.SqlStatisticsModel;
import ru.akvine.iskra.services.domain.statistics.SqlStatisticsService;
import ru.akvine.iskra.services.domain.statistics.dto.CreateStatisticAction;
import ru.akvine.iskra.services.domain.statistics.dto.SearchStatisticsContext;
import ru.akvine.iskra.services.domain.statistics.dto.UpdateStatisticAction;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.domain.table.TableService;
import ru.akvine.iskra.services.domain.table.configuration.TableConfigurationService;
import ru.akvine.iskra.services.domain.table.configuration.dto.UpdateTableConfiguration;
import ru.akvine.iskra.services.dto.ProcessPayload;
import ru.akvine.iskra.services.facades.ScriptsFacade;
import ru.akvine.iskra.services.integration.visor.VisorService;

import java.util.*;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class SyncScriptsFacade implements ScriptsFacade {
    private final TableConfigurationService tableConfigurationService;
    private final VisorService visorService;
    private final SqlStatisticsService sqlStatisticsService;
    private final TableService tableService;

    @Override
    public void disableSqlObjects(ProcessPayload payload) {
        Asserts.isNotNull(payload);

        String processUuid = payload.getProcessUuid();
        boolean resume = payload.isResume();
        PlanModel plan = payload.getPlan();
        Map<TableName, TableModel> selectedTables = payload.getSelectedTables();

        Collection<TableModel> tablesToDisable;
        Map<Long, SqlStatisticsModel> statistics;
        if (resume) {
            SearchStatisticsContext context = new SearchStatisticsContext()
                    .setStates(EnumSet.of(ProcessState.FAILED, ProcessState.STOPPED))
                    .setProcessUuid(processUuid)
                    .setSqlScriptType(SqlScriptType.DISABLE);
            List<SqlStatisticsModel> result = sqlStatisticsService.get(context);

            statistics = result.stream().collect(toMap(SqlStatisticsModel::getTableId, identity()));
            tablesToDisable = tableService.get(plan.getUuid(),
                    result.stream().map(SqlStatisticsModel::getTableId).toList());
        } else {
            statistics = new HashMap<>();
            tablesToDisable = selectedTables.values();
            tablesToDisable
                    .forEach(table -> {
                        CreateStatisticAction action = new CreateStatisticAction()
                                .setScriptType(SqlScriptType.DISABLE)
                                .setProcessUuid(processUuid)
                                .setTableName(table.getTableName())
                                .setTableId(table.getId());
                        SqlStatisticsModel statistic = sqlStatisticsService.create(action);
                        statistics.put(table.getId(), statistic);
                    });
        }

        tablesToDisable.forEach(table -> {
            UpdateStatisticAction action = new UpdateStatisticAction()
                    .setUuid(statistics.get(table.getId()).getUuid());

            try {
                visorService.executeScripts(
                        table.getConfiguration().getDropScripts(),
                        plan.getConnection()
                );

                action.setState(ProcessState.COMPLETED);
                sqlStatisticsService.update(action);
            } catch (RuntimeException exception) {
                // Обновляем статусы для всех статистик в рамках процесса в STOPPED (кроме завершенных = COMPLETED)
                sqlStatisticsService.updateToStatus(
                        processUuid,
                        SqlScriptType.DISABLE,
                        ProcessState.STOPPED,
                        EnumSet.of(ProcessState.COMPLETED)
                );

                // Обновляем статус статистики для проблемной таблицы в FAILED
                action.setState(ProcessState.FAILED);
                sqlStatisticsService.update(action);
            }
        });
    }

    @Override
    public void clearTables(ProcessPayload payload) {
        Asserts.isNotNull(payload);

        String processUuid = payload.getProcessUuid();
        boolean resume = payload.isResume();
        PlanModel plan = payload.getPlan();
        Map<TableName, TableModel> selectedTables = payload.getSelectedTables();

        Collection<TableModel> tablesToDisable;
        Map<Long, SqlStatisticsModel> statistics;
        if (resume) {
            SearchStatisticsContext context = new SearchStatisticsContext()
                    .setStates(EnumSet.of(ProcessState.FAILED, ProcessState.STOPPED))
                    .setProcessUuid(processUuid)
                    .setSqlScriptType(SqlScriptType.CLEAR);
            List<SqlStatisticsModel> result = sqlStatisticsService.get(context);

            statistics = result.stream().collect(toMap(SqlStatisticsModel::getTableId, identity()));
            tablesToDisable = tableService.get(plan.getUuid(),
                    result.stream().map(SqlStatisticsModel::getTableId).toList());
        } else {
            statistics = new HashMap<>();
            tablesToDisable = selectedTables.values();
            tablesToDisable
                    .forEach(table -> {
                        CreateStatisticAction action = new CreateStatisticAction()
                                .setScriptType(SqlScriptType.CLEAR)
                                .setProcessUuid(processUuid)
                                .setTableName(table.getTableName())
                                .setTableId(table.getId());
                        SqlStatisticsModel statistic = sqlStatisticsService.create(action);
                        statistics.put(table.getId(), statistic);
                    });
        }

        tablesToDisable.forEach(table -> {
            UpdateStatisticAction action = new UpdateStatisticAction()
                    .setUuid(statistics.get(table.getId()).getUuid());

            try {
                visorService.executeScripts(
                        table.getConfiguration().getDropScripts(),
                        plan.getConnection()
                );

                action.setState(ProcessState.COMPLETED);
                sqlStatisticsService.update(action);
            } catch (RuntimeException exception) {
                // Обновляем статусы для всех статистик в рамках процесса в STOPPED (кроме завершенных = COMPLETED)
                sqlStatisticsService.updateToStatus(
                        processUuid,
                        SqlScriptType.CLEAR,
                        ProcessState.STOPPED,
                        EnumSet.of(ProcessState.COMPLETED)
                );

                // Обновляем статус статистики для проблемной таблицы в FAILED
                action.setState(ProcessState.FAILED);
                sqlStatisticsService.update(action);
            }
        });
    }

    // TODO: подумать над тем, чтоб вынести повторяющийся код для методов disableSqlObjects, enableSqlObjects, clearTables
    // TODO: в абстрактный класс
    @Override
    public void enableSqlObjects(ProcessPayload payload) {
        Asserts.isNotNull(payload);

        String processUuid = payload.getProcessUuid();
        boolean resume = payload.isResume();
        PlanModel plan = payload.getPlan();
        Map<TableName, TableModel> selectedTables = payload.getSelectedTables();

        Collection<TableModel> tablesToDisable;
        Map<Long, SqlStatisticsModel> statistics;
        if (resume) {
            SearchStatisticsContext context = new SearchStatisticsContext()
                    .setStates(EnumSet.of(ProcessState.FAILED, ProcessState.STOPPED))
                    .setProcessUuid(processUuid)
                    .setSqlScriptType(SqlScriptType.ENABLE);
            List<SqlStatisticsModel> result = sqlStatisticsService.get(context);

            statistics = result.stream().collect(toMap(SqlStatisticsModel::getTableId, identity()));
            tablesToDisable = tableService.get(plan.getUuid(),
                    result.stream().map(SqlStatisticsModel::getTableId).toList());
        } else {
            statistics = new HashMap<>();
            tablesToDisable = selectedTables.values();
            tablesToDisable
                    .forEach(table -> {
                        CreateStatisticAction action = new CreateStatisticAction()
                                .setScriptType(SqlScriptType.ENABLE)
                                .setProcessUuid(processUuid)
                                .setTableName(table.getTableName())
                                .setTableId(table.getId());
                        SqlStatisticsModel statistic = sqlStatisticsService.create(action);
                        statistics.put(table.getId(), statistic);
                    });
        }

        tablesToDisable.forEach(table -> {
            UpdateStatisticAction action = new UpdateStatisticAction()
                    .setUuid(statistics.get(table.getId()).getUuid());

            try {
                visorService.executeScripts(
                        table.getConfiguration().getDropScripts(),
                        plan.getConnection()
                );

                action.setState(ProcessState.COMPLETED);
                sqlStatisticsService.update(action);
            } catch (RuntimeException exception) {
                // Обновляем статусы для всех статистик в рамках процесса в STOPPED (кроме завершенных = COMPLETED)
                sqlStatisticsService.updateToStatus(
                        processUuid,
                        SqlScriptType.ENABLE,
                        ProcessState.STOPPED,
                        EnumSet.of(ProcessState.COMPLETED)
                );

                // Обновляем статус статистики для проблемной таблицы в FAILED
                action.setState(ProcessState.FAILED);
                sqlStatisticsService.update(action);
            }
        });
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
}
