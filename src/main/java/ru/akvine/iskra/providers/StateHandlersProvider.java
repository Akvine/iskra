package ru.akvine.iskra.providers;

import java.util.Map;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.exceptions.plan.UnknownPlanStateException;
import ru.akvine.iskra.services.state_machine.handlers.PlanStateHandler;

public record StateHandlersProvider(Map<PlanState, PlanStateHandler> handlers) {
    public PlanStateHandler getByState(PlanState state, boolean resume) {
        //        if (!resume || state == PlanState.COMPLETED) {
        //            return handlers.get(PlanState.STARTED);
        //        }

        if (handlers.containsKey(state)) {
            return handlers.get(state);
        }

        throw new UnknownPlanStateException("Unknown plan state = [" + state + "]!");
    }
}
