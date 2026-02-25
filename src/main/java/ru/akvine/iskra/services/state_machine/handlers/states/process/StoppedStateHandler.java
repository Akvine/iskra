package ru.akvine.iskra.services.state_machine.handlers.states.process;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.services.GeneratorCacheService;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.state_machine.handlers.AbstractStateHandler;

@Service
public class StoppedStateHandler extends AbstractStateHandler {
    private final GeneratorCacheService generatorCacheService;

    @Autowired
    protected StoppedStateHandler(PlanService planService, GeneratorCacheService generatorCacheService) {
        super(planService);
        this.generatorCacheService = generatorCacheService;
    }

    @Override
    public void doHandle(
            PlanModel plan, Map<TableName, TableModel> selectedTables, boolean resume, String processUuid) {
        generatorCacheService.remove(plan.getUuid());
    }

    @Override
    public PlanState getCurrentState() {
        return PlanState.STOPPED;
    }

    @Override
    public PlanState toNextState() {
        return PlanState.IN_PROGRESS;
    }

    @Override
    public PlanState toFailedStateIfError() {
        return PlanState.STOPPED;
    }
}
