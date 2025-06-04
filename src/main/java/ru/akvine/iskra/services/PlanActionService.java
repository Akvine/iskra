package ru.akvine.iskra.services;

import ru.akvine.iskra.services.dto.plan.action.StartAction;

public interface PlanActionService {

    String start(StartAction action);

    boolean stop(String planUuid);
}
