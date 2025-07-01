package ru.akvine.iskra.services.domain.table;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.akvine.iskra.repositories.entities.TableEntity;
import ru.akvine.iskra.services.domain.table.dto.ToogleSelectedTables;
import ru.akvine.iskra.services.domain.table.dto.ListTables;

import java.util.List;

public interface TableService {
    @Transactional
    List<TableModel> getAll(String planUuid, String userUuid);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    TableEntity save(TableEntity tableToSave);

    TableEntity verifyExistsBy(String planUuid, String name);

    @Transactional
    List<TableModel> list(ListTables listTables);

    @Transactional
    List<TableModel> toggleSelected(ToogleSelectedTables action);
}
