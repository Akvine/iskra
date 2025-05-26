package ru.akvine.iskra.services;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.akvine.iskra.repositories.entities.TableEntity;
import ru.akvine.iskra.services.domain.TableModel;
import ru.akvine.iskra.services.dto.table.ToogleSelectedTables;
import ru.akvine.iskra.services.dto.table.ListTables;

import java.util.List;

public interface TableService {
    List<TableModel> getAll(String planUuid);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    TableEntity save(TableEntity tableToSave);

    TableEntity verifyExistsByName(String name);

    @Transactional
    List<TableModel> list(ListTables listTables);

    @Transactional
    List<TableModel> toggleSelected(ToogleSelectedTables action);
}
