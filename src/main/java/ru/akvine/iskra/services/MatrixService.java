package ru.akvine.iskra.services;

import ru.akvine.iskra.services.domain.plan.PlanModel;

import java.util.List;

public interface MatrixService {
    List<String> detectIndependent(PlanModel plan);
}
