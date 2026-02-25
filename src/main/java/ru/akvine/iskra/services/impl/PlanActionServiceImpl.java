package ru.akvine.iskra.services.impl;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.exceptions.table.AnyTablesNotSelectedException;
import ru.akvine.iskra.repositories.entities.PlanEntity;
import ru.akvine.iskra.services.PlanActionService;
import ru.akvine.iskra.services.ScriptGeneratorService;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.domain.plan.dto.action.GenerateScriptsResult;
import ru.akvine.iskra.services.domain.plan.dto.action.StartAction;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.domain.table.TableService;
import ru.akvine.iskra.services.domain.table.dto.ListTables;
import ru.akvine.iskra.services.state_machine.managers.PlanManager;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanActionServiceImpl implements PlanActionService {
    private final TableService tableService;
    private final PlanService planService;
    private final PlanManager planManager;
    private final ScriptGeneratorService scriptGeneratorService;

    @Override
    public String start(StartAction action) {
        Asserts.isNotNull(action);

        String planUuid = action.getPlanUuid();
        PlanEntity plan = planService.verifyExists(planUuid, action.getUserUuid());

        return planManager.start(new PlanModel(plan), action.isResume());
    }

    @Override
    public boolean stop(String planUuid, String userUuid) {
        PlanModel plan = new PlanModel(planService.verifyExists(planUuid, userUuid));
        return planManager.stop(plan);
    }

    @Override
    public GenerateScriptsResult generateScripts(String planUuid, String userUuid) {
        Asserts.isNotBlank(planUuid);
        Asserts.isNotBlank(userUuid);

        PlanEntity plan = planService.verifyExists(planUuid, userUuid);

        ListTables listTables =
                new ListTables().setUserUuid(userUuid).setPlanUuid(planUuid).setSelected(true);
        Map<TableName, TableModel> selectedTables = tableService.list(listTables).stream()
                .collect(Collectors.toMap(table -> new TableName(table.getTableName()), Function.identity()));

        if (CollectionUtils.isEmpty(selectedTables)) {
            String message = "For plan = [" + plan.getName() + "] not selected any tables to generate scripts!";
            throw new AnyTablesNotSelectedException(message);
        }

        return scriptGeneratorService.generateScripts(new PlanModel(plan), selectedTables);
    }
}
