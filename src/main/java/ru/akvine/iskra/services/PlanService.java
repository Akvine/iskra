package ru.akvine.iskra.services;

import ru.akvine.iskra.repositories.entities.PlanEntity;
import ru.akvine.iskra.services.domain.PlanModel;

import java.util.List;

public interface PlanService {
    PlanModel create();

    PlanModel get(String uuid);

    List<PlanModel> list();

    PlanEntity verifyExists(String byUuid);
}
