package ru.akvine.iskra.services;

import org.springframework.transaction.annotation.Transactional;
import ru.akvine.iskra.services.domain.plan.dto.action.GenerateScriptsResult;
import ru.akvine.iskra.services.domain.plan.dto.action.StartAction;

public interface PlanActionService {

    String start(StartAction action);

    boolean stop(String planUuid, String userUuid);

    @Transactional
    // TODO: обращение к внешнему сервису под Transactional будет держать коннекты к БД открытыми
    GenerateScriptsResult generateScripts(String planUuid, String userUuid);
}
