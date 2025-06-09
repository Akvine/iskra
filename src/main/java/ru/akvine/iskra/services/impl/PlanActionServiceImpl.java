package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.exceptions.table.AnyTablesNotSelectedException;
import ru.akvine.iskra.exceptions.table.configuration.TableConfigurationNotFoundException;
import ru.akvine.iskra.services.GeneratorCacheService;
import ru.akvine.iskra.services.GeneratorFacade;
import ru.akvine.iskra.services.PlanActionService;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.domain.table.TableService;
import ru.akvine.iskra.services.dto.plan.action.StartAction;
import ru.akvine.iskra.services.dto.table.ListTables;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanActionServiceImpl implements PlanActionService {
    private final TableService tableService;
    private final PlanService planService;
    private final GeneratorFacade generatorFacade;
    private final GeneratorCacheService generatorCacheService;

    @Override
    public String start(StartAction action) {
        Asserts.isNotNull(action);

        String planUuid = action.getPlanUuid();
        planService.verifyExists(planUuid, action.getUserUuid());

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

        selectedTables.values().forEach(table -> {
            if (table.getConfiguration() == null) {
                String errorMessage = String.format("Table with name = [%s] has no configuration!", table.getTableName());
                throw new TableConfigurationNotFoundException(errorMessage);
            }
        });

        return generatorFacade.generate(planUuid, selectedTables, action.isResume());
    }

    @Override
    public boolean stop(String planUuid) {
        if (StringUtils.isBlank(planUuid)) {
            throw new IllegalArgumentException("Plan uuid can't be null!");
        }

        generatorCacheService.stop(planUuid);
        log.info("Plan with uuid = [{}] was successfully stopped!", planUuid);
        return true;
    }
}
