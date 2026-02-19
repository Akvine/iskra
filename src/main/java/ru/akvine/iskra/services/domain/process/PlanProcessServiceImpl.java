package ru.akvine.iskra.services.domain.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.compozit.commons.utils.UUIDGenerator;
import ru.akvine.iskra.components.SecurityManager;
import ru.akvine.iskra.exceptions.process.PlanProcessNotFoundException;
import ru.akvine.iskra.repositories.PlanProcessRepository;
import ru.akvine.iskra.repositories.entities.PlanEntity;
import ru.akvine.iskra.repositories.entities.PlanProcessEntity;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.domain.process.dto.CreatePlanProcess;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanProcessServiceImpl implements PlanProcessService {
    private final PlanProcessRepository planProcessRepository;
    private final PlanService planService;
    private final SecurityManager securityManager;

    @Override
    public PlanProcessModel create(CreatePlanProcess action) {
        Asserts.isNotNull(action);

        PlanEntity plan = planService.verifyExists(action.getPlanUuid(),
                securityManager.getCurrentUser().getUuid());
        PlanProcessEntity entity = new PlanProcessEntity()
                .setUuid(UUIDGenerator.uuid())
                .setTotalTablesCount(action.getTotalTablesCount())
                .setPlan(plan);

        return new PlanProcessModel(planProcessRepository.save(entity));
    }

    @Override
    public PlanProcessEntity verifyExistsBy(String processUuid) {
        return planProcessRepository.findByUuid(processUuid)
                .orElseThrow(() -> new PlanProcessNotFoundException("Plan process with uuid = [" + processUuid + "] not found!"));
    }

    @Override
    public PlanProcessModel getLastStoppedOrFailed(String planUuid) {
        Asserts.isNotBlank(planUuid, "planUuid is null");
        return new PlanProcessModel(planProcessRepository
                .findLastStoppedOrFailed(planUuid)
                .orElseThrow(() -> {
                    String message = String.format(
                            "Failed or stopped process for plan = [%s] not found!", planUuid
                    );
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
    public PlanProcessModel getOrNull(String processUuid) {
        try {
            return new PlanProcessModel(verifyExistsBy(processUuid));
        } catch (PlanProcessNotFoundException exception) {
            log.debug("Plan process with uuid = [{}] not found! Return null", processUuid);
            return null;
        }
    }
}
