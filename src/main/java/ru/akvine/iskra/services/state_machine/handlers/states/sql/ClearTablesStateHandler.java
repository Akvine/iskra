package ru.akvine.iskra.services.state_machine.handlers.states.sql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.providers.StateHandlersProvider;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.dto.ProcessPayload;
import ru.akvine.iskra.services.facades.ScriptsFacade;
import ru.akvine.iskra.services.state_machine.handlers.AbstractStateHandler;

import java.util.Map;

@Component
@Slf4j
public class ClearTablesStateHandler extends AbstractStateHandler {
    private final ScriptsFacade scriptsFacade;

    @Autowired
    protected ClearTablesStateHandler(PlanService planService,
                                      StateHandlersProvider stateHandlersProvider,
                                      ScriptsFacade scriptsFacade) {
        super(planService, stateHandlersProvider);
        this.scriptsFacade = scriptsFacade;
    }

    @Override
    public void doHandle(PlanModel plan,
                         Map<TableName, TableModel> selectedTables,
                         boolean resume,
                         String processUuid) {
        ProcessPayload payload = new ProcessPayload()
                .setPlan(plan)
                .setSelectedTables(selectedTables)
                .setResume(resume)
                .setProcessUuid(processUuid);
        scriptsFacade.clearTables(payload);

//        selectedTables.forEach((tableName, table) -> {
//            TableProcessModel tableProcess;
//            if (resume) {
//                tableProcess = tableProcessService.get(processUuid, table.getTableName());
//                UpdateTableProcess updateAction = new UpdateTableProcess()
//                        .setProcessUuid(processUuid)
//                        .setTableName(table.getTableName())
//                        .setState(ProcessState.IN_PROGRESS);
//                tableProcessService.update(updateAction);
//            } else {
//                CreateTableProcess createTableProcessAction = new CreateTableProcess()
//                        .setPlanUuid(table.getPlan().getUuid())
//                        .setUserUuid(plan.getUser().getUuid())
//                        .setProcessUuid(processUuid)
//                        .setTableName(table.getTableName())
//                        .setTotalRowsCount(table.getConfiguration().getRowsCount());
//                tableProcess = tableProcessService.create(createTableProcessAction);
//            }
//
//            TableConfigurationModel configuration = table.getConfiguration();
//            if (StringUtils.isNotBlank(configuration.getClearScript()) && !resume) {
//                log.info("Execute clear script for table = [{}], plan uuid = [{}] and name = [{}]",
//                        table.getTableName(), plan.getUuid(), plan.getName());
//                try {
//                    scriptsFacade.executeScript(plan.getConnection(), configuration.getClearScript());
//                } catch (RuntimeException exception) {
//                    UpdateTableProcess updateTableProcessAction = new UpdateTableProcess()
//                            .setPid(tableProcess.getPid())
//                            .setAddSuccessRowsCount(null)
//                            .setErrorMessage(exception.getMessage())
//                            .setState(ProcessState.FAILED);
//                    tableProcessService.update(updateTableProcessAction);
//                    throw exception;
//                }
//            }
//        });
    }


    @Override
    public PlanState getCurrentState() {
        return PlanState.CLEAR_TABLES;
    }

    @Override
    public PlanState toNextState() {
        return PlanState.DISABLE_SQL_OBJECTS;
    }

    @Override
    public PlanState toFailedStateIfError() {
        return PlanState.CLEAR_TABLES_FAILED;
    }
}
