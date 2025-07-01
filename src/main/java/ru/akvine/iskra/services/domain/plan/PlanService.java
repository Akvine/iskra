package ru.akvine.iskra.services.domain.plan;

import ru.akvine.iskra.repositories.entities.PlanEntity;
import ru.akvine.iskra.services.domain.plan.dto.CreatePlan;
import ru.akvine.iskra.services.domain.plan.dto.DuplicatePlan;
import ru.akvine.iskra.services.domain.plan.dto.UpdatePlan;

import java.util.List;

public interface PlanService {

    PlanModel create(CreatePlan createPlan);

    PlanModel duplicate(DuplicatePlan duplicatePlan);

    PlanModel get(String uuid, String userUuid);

    List<PlanModel> list(String userUuid);

    PlanEntity verifyExists(String byUuid, String byUserUuid);

    PlanModel update(UpdatePlan action);
}
