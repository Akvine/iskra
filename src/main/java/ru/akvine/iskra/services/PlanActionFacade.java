package ru.akvine.iskra.services;

import jakarta.annotation.Nullable;
import ru.akvine.compozit.commons.ConnectionDto;
import ru.akvine.iskra.events.GenerateDataEvent;
import ru.akvine.iskra.services.domain.TableModel;
import ru.akvine.iskra.services.domain.TableProcessModel;
import ru.akvine.iskra.services.dto.GenerateDataAction;

public interface PlanActionFacade {
    void handleEvent(GenerateDataEvent event);

    @Nullable
    String generateData(GenerateDataAction action, ConnectionDto connection);

    TableProcessModel generateData(String processUuid, TableModel table);
}
