package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.compozit.commons.utils.UUIDGenerator;
import ru.akvine.iskra.exceptions.ProcessNotFoundException;
import ru.akvine.iskra.repositories.PlanRepository;
import ru.akvine.iskra.repositories.entities.PlanEntity;
import ru.akvine.iskra.services.PlanService;
import ru.akvine.iskra.services.domain.PlanModel;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;

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
}
