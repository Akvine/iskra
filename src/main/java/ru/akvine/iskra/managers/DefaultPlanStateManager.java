package ru.akvine.iskra.managers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.providers.StateHandlersProvider;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.process.PlanProcessModel;
import ru.akvine.iskra.services.domain.process.PlanProcessService;
import ru.akvine.iskra.services.domain.process.dto.CreatePlanProcess;
import ru.akvine.iskra.services.domain.table.TableModel;

import java.util.EnumSet;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DefaultPlanStateManager implements PlanStateManager {
    private static final Logger log = LoggerFactory.getLogger(DefaultPlanStateManager.class);
    private final StateHandlersProvider stateHandlersProvider;
    private final PlanProcessService planProcessService;

    @Override
    public void manage(PlanModel planModel,
                       Map<TableName, TableModel> selectedTables,
                       boolean resume,
                       String planProcessUuid) {
        log.debug("Manage plan with name = [{}] and uuid = [{}]", planModel.getName(), planModel.getUser());

        PlanProcessModel planProcess = planProcessService.getOrNull(planProcessUuid);
        if (planProcess == null) {
            // Создаем новую сущность процесса генерации
            CreatePlanProcess action = new CreatePlanProcess()
                    .setPlanUuid(planModel.getUuid())
                    .setTotalTablesCount(selectedTables.size())
                    .setProcessUuid(planProcessUuid);
            planProcess = planProcessService.create(action);
            planProcessService.start(planProcess);
        }

        try {
            PlanModel plan = planModel;

            while (!EnumSet.of(PlanState.COMPLETED, PlanState.STOPPED).contains(plan.getPlanState())
                    || plan.getPlanState() == null) {
                plan = stateHandlersProvider.getByState(planModel.getPlanState(), resume)
                        .process(planModel, selectedTables, resume, planProcessUuid);
            }
        } catch (Exception exception) {
            log.info("Error was occurred for plan = [{}], process = [{}]. Message = [{}]",
                    planModel.getName(), planProcessUuid, exception.getMessage());
            planProcessService.toFail(planProcess, exception.getMessage());
        }

    }
}
