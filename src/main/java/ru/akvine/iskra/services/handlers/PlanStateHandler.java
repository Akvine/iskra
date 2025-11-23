package ru.akvine.iskra.services.handlers;

import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.services.domain.plan.PlanModel;

public interface PlanStateHandler {
    void process(PlanModel planModel, boolean continueProcess);

    PlanState getCurrentState();

    PlanState toNextState();

    PlanState toFailedStateIfError();
}
