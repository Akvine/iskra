package ru.akvine.iskra.services;

import org.springframework.transaction.annotation.Transactional;
import ru.akvine.iskra.services.dto.plan.action.GenerateScriptsResult;
import ru.akvine.iskra.services.dto.plan.action.StartAction;

public interface PlanActionService {

    String start(StartAction action);

    boolean stop(String planUuid);

    @Transactional
    // TODO: обращение к внешнему сервису под Transactional будет держать коннекты к БД открытыми
    GenerateScriptsResult generateScripts(String planUuid, String userUuid);
}
