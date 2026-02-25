package ru.akvine.iskra.services.domain.table.process;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import ru.akvine.iskra.repositories.entities.TableProcessEntity;
import ru.akvine.iskra.services.domain.table.process.dto.CreateTableProcess;
import ru.akvine.iskra.services.domain.table.process.dto.ListTableProcess;
import ru.akvine.iskra.services.domain.table.process.dto.UpdateTableProcess;

public interface TableProcessService {
    TableProcessModel create(CreateTableProcess createTableProcess);

    @Transactional
    TableProcessModel update(UpdateTableProcess updateTableProcess);

    @Transactional
    TableProcessModel get(String processUuid, String tableName);

    @Transactional
    TableProcessEntity verifyExists(String byProcessUuid, String byTableName);

    @Transactional
    List<TableProcessModel> list(ListTableProcess listTableProcess);
}
