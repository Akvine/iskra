package ru.akvine.iskra.managers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.providers.StateHandlersProvider;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.table.TableModel;

import java.util.EnumSet;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DefaultPlanStateManager implements PlanStateManager {
    private final StateHandlersProvider stateHandlersProvider;

    @Override
    public void manage(PlanModel planModel,
                       Map<TableName, TableModel> selectedTables,
                       boolean resume,
                       String processUuid) {

        PlanModel plan = planModel;
        while (!EnumSet.of(PlanState.COMPLETED, PlanState.STOPPED).contains(plan.getPlanState()) || plan.getPlanState() == null) {
            plan = stateHandlersProvider.getByState(planModel.getPlanState(), resume)
                    .process(planModel, selectedTables, resume, processUuid);
        }

    }
}
