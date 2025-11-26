package ru.akvine.iskra.services.state_machine.managers;

import ru.akvine.iskra.services.domain.plan.PlanModel;

/**
 * Менеджер для планов
 */
public interface PlanManager {
    /**
     * Возвращает UUID последнего запущенного процесса.
     * @param plan план
     * @return {@link java.util.UUID} последнего запущенного процесса
     */
    String start(PlanModel plan, boolean resume);

    boolean stop(PlanModel plan);
}
