package ru.akvine.iskra.services.state_machine.handlers.states.sql;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.dto.ProcessPayload;
import ru.akvine.iskra.services.facades.ScriptsFacade;
import ru.akvine.iskra.services.state_machine.handlers.AbstractStateHandler;

@Component
public class DisableSqlObjectsStateHandler extends AbstractStateHandler {
    private final ScriptsFacade scriptsFacade;

    @Autowired
    protected DisableSqlObjectsStateHandler(PlanService planService, ScriptsFacade scriptsFacade) {
        super(planService);
        this.scriptsFacade = scriptsFacade;
    }

    @Override
    public void doHandle(
            PlanModel plan, Map<TableName, TableModel> selectedTables, boolean resume, String processUuid) {
        ProcessPayload payload = new ProcessPayload()
                .setPlan(plan)
                .setSelectedTables(selectedTables)
                .setResume(resume)
                .setProcessUuid(processUuid);
        scriptsFacade.disableSqlObjects(payload);
    }

    @Override
    public PlanState getCurrentState() {
        return PlanState.DISABLE_SQL_OBJECTS;
    }

    @Override
    public PlanState toNextState() {
        return PlanState.CLEAR_TABLES;
    }

    @Override
    public PlanState toFailedStateIfError() {
        return PlanState.DISABLE_SQL_OBJECTS_FAILED;
    }
}
