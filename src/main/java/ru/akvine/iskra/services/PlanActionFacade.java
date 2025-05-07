package ru.akvine.iskra.services;

import ru.akvine.compozit.commons.ConnectionDto;
import ru.akvine.compozit.commons.TableConfig;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.events.GenerateDataEvent;
import ru.akvine.iskra.services.domain.TableProcessModel;
import ru.akvine.iskra.services.dto.GenerateDataAction;

public interface PlanActionFacade {
    void handleEvent(GenerateDataEvent event);

    void generateData(GenerateDataAction action);

    TableProcessModel generateData(String processPid,
                                   TableName tableName,
                                   TableConfig config,
                                   ConnectionDto connection);
}
