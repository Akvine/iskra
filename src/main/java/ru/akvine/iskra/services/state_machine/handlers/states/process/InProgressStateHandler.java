package ru.akvine.iskra.services.state_machine.handlers.states.process;

import java.util.Map;
import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.domain.table.process.TableProcessService;
import ru.akvine.iskra.services.domain.table.process.dto.CreateTableProcess;
import ru.akvine.iskra.services.facades.DataGeneratorFacade;
import ru.akvine.iskra.services.state_machine.handlers.AbstractStateHandler;

@Component
public class InProgressStateHandler extends AbstractStateHandler {
    private final DataGeneratorFacade dataGeneratorFacade;
    private final TableProcessService tableProcessService;

    protected InProgressStateHandler(
            PlanService planService, DataGeneratorFacade dataGeneratorFacade, TableProcessService tableProcessService) {
        super(planService);
        this.dataGeneratorFacade = dataGeneratorFacade;
        this.tableProcessService = tableProcessService;
    }

    @Override
    public void doHandle(
            PlanModel plan, Map<TableName, TableModel> selectedTables, boolean resume, String processUuid) {
        selectedTables.forEach((tableName, table) -> {
            CreateTableProcess action = new CreateTableProcess();
            action.setTableName(tableName.getName())
                    .setPlanUuid(plan.getUuid())
                    .setProcessUuid(processUuid)
                    .setTotalRowsCount(table.getConfiguration().getRowsCount())
                    .setUserUuid(plan.getUser().getUuid());
            tableProcessService.create(action);
        });

        selectedTables.forEach((tableName, table) -> dataGeneratorFacade.generate(processUuid, table, resume));
    }

    @Override
    public PlanState getCurrentState() {
        return PlanState.IN_PROGRESS;
    }

    @Override
    public PlanState toNextState() {
        return PlanState.ENABLE_SQL_OBJECTS;
    }

    @Override
    public PlanState toFailedStateIfError() {
        return PlanState.FAILED;
    }
}
