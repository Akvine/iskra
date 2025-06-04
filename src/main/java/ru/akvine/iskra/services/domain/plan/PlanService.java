package ru.akvine.iskra.services.domain.plan;

import ru.akvine.iskra.repositories.entities.PlanEntity;
import ru.akvine.iskra.services.dto.plan.CreatePlan;
import ru.akvine.iskra.services.dto.plan.UpdatePlan;

import java.util.List;

public interface PlanService {

    PlanModel create(CreatePlan createPlan);

    PlanModel get(String uuid, String userUuid);

    List<PlanModel> list(String userUuid);

    PlanEntity verifyExists(String byUuid, String byUserUuid);

    PlanModel update(UpdatePlan action);
}
