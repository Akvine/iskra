package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.ConnectionDto;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.compozit.commons.utils.UUIDGenerator;
import ru.akvine.iskra.events.GenerateDataEvent;
import ru.akvine.iskra.exceptions.plan.PlanNotFoundException;
import ru.akvine.iskra.repositories.PlanRepository;
import ru.akvine.iskra.repositories.entities.ConnectionEntity;
import ru.akvine.iskra.repositories.entities.PlanEntity;
import ru.akvine.iskra.services.ConnectionService;
import ru.akvine.iskra.services.PlanService;
import ru.akvine.iskra.services.domain.ConnectionModel;
import ru.akvine.iskra.services.domain.PlanModel;
import ru.akvine.iskra.services.dto.GenerateDataAction;
import ru.akvine.iskra.services.dto.plan.CreatePlan;
import ru.akvine.iskra.services.dto.plan.UpdatePlan;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;
    private final ConnectionService connectionService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public String start(GenerateDataAction action) {
        Asserts.isNotNull(action);
        PlanEntity plan = verifyExists(action.getPlanUuid());
        ConnectionDto connection = buildConnectionDto(new ConnectionModel(plan.getConnection()));

        eventPublisher.publishEvent(new GenerateDataEvent(this, action, connection));
        return plan.getUuid();
    }

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

    private ConnectionDto buildConnectionDto(ConnectionModel connectionModel) {
        return new ConnectionDto()
                .setConnectionName(connectionModel.getConnectionName())
                .setDatabaseName(connectionModel.getDatabaseName())
                .setHost(connectionModel.getHost())
                .setSchema(connectionModel.getSchema())
                .setPort(connectionModel.getPort())
                .setUsername(connectionModel.getUsername())
                .setPassword(connectionModel.getPassword())
                .setDatabaseType(connectionModel.getDatabaseType().getValue());
    }
}
