package ru.akvine.iskra.services;

import ru.akvine.compozit.commons.ConnectionDto;
import ru.akvine.compozit.commons.TableConfig;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.services.domain.TableProcessModel;
import ru.akvine.iskra.services.dto.GenerateDataAction;

public interface GenerateDataFacade {
    void generateData(GenerateDataAction action);

    TableProcessModel generateData(TableName tableName, TableConfig config, ConnectionDto connection);
}
