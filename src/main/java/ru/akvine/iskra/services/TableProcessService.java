package ru.akvine.iskra.services;

import ru.akvine.compozit.commons.TableConfig;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.repositories.entities.TableProcessEntity;
import ru.akvine.iskra.services.domain.TableProcessModel;

public interface TableProcessService {
    TableProcessModel create(TableName tableName, TableConfig config);

    TableProcessModel get(String pid);

    TableProcessEntity verifyExists(String byPid);
}
