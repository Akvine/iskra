package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.ConnectionDto;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.compozit.commons.utils.UUIDGenerator;
import ru.akvine.iskra.events.GenerateDataEvent;
import ru.akvine.iskra.exceptions.ProcessNotFoundException;
import ru.akvine.iskra.repositories.PlanRepository;
import ru.akvine.iskra.repositories.entities.PlanEntity;
import ru.akvine.iskra.services.ConnectionService;
import ru.akvine.iskra.services.PlanService;
import ru.akvine.iskra.services.domain.ConnectionModel;
import ru.akvine.iskra.services.domain.PlanModel;
import ru.akvine.iskra.services.dto.GenerateDataAction;

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
        String planUuid = action.getPlanUuid();
        verifyExists(planUuid);
        ConnectionDto connection = buildConnectionDto(connectionService.get(action.getConnectionName()));

        eventPublisher.publishEvent(new GenerateDataEvent(this, action, connection));
        return planUuid;
    }

    @Override
    public PlanModel create() {
        PlanEntity process = new PlanEntity().setUuid(UUIDGenerator.uuid());
        return new PlanModel(planRepository.save(process));
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
                .orElseThrow(() -> new ProcessNotFoundException("Process with uuid = [" + byUuid + "] is not found!"));
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
