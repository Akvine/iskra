package ru.akvine.iskra.services.domain.table.process;

import ru.akvine.iskra.repositories.entities.TableProcessEntity;
import ru.akvine.iskra.services.dto.process.CreateTableProcess;
import ru.akvine.iskra.services.dto.process.ListTableProcess;
import ru.akvine.iskra.services.dto.process.UpdateTableProcess;

import java.util.List;

public interface TableProcessService {
    TableProcessModel create(CreateTableProcess createTableProcess);

    TableProcessModel update(UpdateTableProcess updateTableProcess);

    TableProcessModel get(String pid);

    TableProcessModel get(String processUuid, String tableName);

    TableProcessEntity verifyExists(String byProcessUuid, String byTableName);

    List<TableProcessModel> list(ListTableProcess listTableProcess);

    TableProcessEntity verifyExists(String byPid);
}
