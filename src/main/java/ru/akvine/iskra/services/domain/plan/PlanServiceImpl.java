package ru.akvine.iskra.services.domain.plan;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.compozit.commons.utils.UUIDGenerator;
import ru.akvine.iskra.exceptions.plan.PlanNotFoundException;
import ru.akvine.iskra.repositories.PlanRepository;
import ru.akvine.iskra.repositories.entities.ConnectionEntity;
import ru.akvine.iskra.repositories.entities.PlanEntity;
import ru.akvine.iskra.services.domain.connection.ConnectionService;
import ru.akvine.iskra.services.dto.plan.CreatePlan;
import ru.akvine.iskra.services.dto.plan.UpdatePlan;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;
    private final ConnectionService connectionService;

    @Override
    public PlanModel create(CreatePlan createPlan) {
        Asserts.isNotNull(createPlan);
        ConnectionEntity connection = connectionService.verifyExists(createPlan.getConnectionName());

        PlanEntity plan = new PlanEntity()
                .setUuid(UUIDGenerator.uuidWithoutDashes())
                .setName(createPlan.getName())
                .setConnection(connection);
        return new PlanModel(planRepository.save(plan));
    }

    @Override
    public PlanModel get(String uuid) {
        return new PlanModel(verifyExists(uuid));
    }

    @Override
    public List<PlanModel> list() {
        return planRepository
                .findAll().stream()
                .map(PlanModel::new)
                .toList();
    }

    @Override
    public PlanEntity verifyExists(String byUuid) {
        Asserts.isNotNull(byUuid);
        return planRepository
                .findByUuid(byUuid)
                .orElseThrow(() -> new PlanNotFoundException("Plan with uuid = [" + byUuid + "] is not found!"));
    }

    @Override
    public PlanModel update(UpdatePlan action) {
        Asserts.isNotNull(action);

        PlanEntity planToUpdate = verifyExists(action.getPlanUuid());

        if (StringUtils.isNotBlank(action.getLastProcessUuid()) &&
                !action.getLastProcessUuid().equals(planToUpdate.getLastProcessUuid())) {
            planToUpdate.setLastProcessUuid(planToUpdate.getLastProcessUuid());
        }

        return new PlanModel(planRepository.save(planToUpdate));
    }
}
