package ru.akvine.iskra.services.domain.process;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.enums.ProcessState;
import ru.akvine.iskra.exceptions.process.PlanProcessNotFoundException;
import ru.akvine.iskra.repositories.PlanProcessRepository;
import ru.akvine.iskra.repositories.entities.PlanEntity;
import ru.akvine.iskra.repositories.entities.PlanProcessEntity;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.domain.process.dto.CreatePlanProcess;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanProcessServiceImpl implements PlanProcessService {
    private final PlanProcessRepository planProcessRepository;
    private final PlanService planService;

    @Override
    public PlanProcessModel create(CreatePlanProcess action) {
        Asserts.isNotNull(action);

        PlanEntity plan = planService.verifyExists(action.getPlanUuid(), action.getUserUuid());
        PlanProcessEntity entity = new PlanProcessEntity()
                .setUuid(action.getProcessUuid())
                .setTotalTablesCount(action.getTotalTablesCount())
                .setPlan(plan);

        return new PlanProcessModel(planProcessRepository.save(entity));
    }

    @Override
    public PlanProcessEntity verifyExistsBy(String processUuid) {
        return planProcessRepository
                .findByUuid(processUuid)
                .orElseThrow(() ->
                        new PlanProcessNotFoundException("Plan process with uuid = [" + processUuid + "] not found!"));
    }

    @Override
    public PlanProcessModel getLastStoppedOrFailed(String planUuid) {
        Asserts.isNotBlank(planUuid, "planUuid is null");
        return new PlanProcessModel(
                planProcessRepository.findLastStoppedOrFailed(planUuid).orElseThrow(() -> {
                    String message = String.format("Failed or stopped process for plan = [%s] not found!", planUuid);
                    return new PlanProcessNotFoundException(message);
                }));
    }

    @Override
    public PlanProcessModel start(PlanProcessModel planProcess) {
        PlanProcessEntity process = verifyExistsBy(planProcess.getUuid());
        process.setStartedDate(new Date());
        process.setUpdatedDate(new Date());
        return new PlanProcessModel(planProcessRepository.save(process));
    }

    @Override
    public PlanProcessModel toFail(PlanProcessModel planProcess, String errorMessage) {
        PlanProcessEntity process = verifyExistsBy(planProcess.getUuid());
        process.setCompletedDate(new Date());
        process.setUpdatedDate(new Date());
        process.setErrorMessage(errorMessage);
        return new PlanProcessModel(planProcessRepository.save(process));
    }

    @Override
    public PlanProcessModel toCompleted(PlanProcessModel planProcessModel) {
        PlanProcessEntity process = verifyExistsBy(planProcessModel.getUuid());
        process.setCompletedDate(new Date());
        process.setUpdatedDate(new Date());
        process.setProcessState(ProcessState.COMPLETED);
        return new PlanProcessModel(planProcessRepository.save(process));
    }

    @Override
    public PlanProcessModel getOrNull(String processUuid) {
        try {
            return new PlanProcessModel(verifyExistsBy(processUuid));
        } catch (PlanProcessNotFoundException exception) {
            log.debug("Plan process with uuid = [{}] not found! Return null", processUuid);
            return null;
        }
    }

    @Override
    public PlanProcessModel get(String processUuid) {
        return new PlanProcessModel(verifyExistsBy(processUuid));
    }
}
