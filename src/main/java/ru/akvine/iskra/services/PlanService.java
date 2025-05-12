package ru.akvine.iskra.services;

import ru.akvine.iskra.repositories.entities.PlanEntity;
import ru.akvine.iskra.services.domain.PlanModel;
import ru.akvine.iskra.services.dto.GenerateDataAction;
import ru.akvine.iskra.services.dto.plan.CreatePlan;

import java.util.List;

public interface PlanService {
    String start(GenerateDataAction action);

    PlanModel create(CreatePlan createPlan);

    PlanModel get(String uuid);

    List<PlanModel> list();

    PlanEntity verifyExists(String byUuid);
}
