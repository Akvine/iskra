package ru.akvine.iskra.providers;


import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.exceptions.plan.UnknownPlanStateException;
import ru.akvine.iskra.services.handlers.PlanStateHandler;

import java.util.Map;

public record StateHandlersProvider(Map<PlanState, PlanStateHandler> handlers) {
    public PlanStateHandler getByState(PlanState state) {
        if (handlers.containsKey(state)) {
            return handlers.get(state);
        }

        throw new UnknownPlanStateException("Unknown plan state = [" + state + "]!");
    }
}
