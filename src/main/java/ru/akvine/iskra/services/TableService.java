package ru.akvine.iskra.services;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.akvine.iskra.repositories.entities.TableEntity;
import ru.akvine.iskra.services.dto.table.TableModel;

import java.util.List;

public interface TableService {
    List<TableModel> getAll(String planUuid);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    TableEntity save(TableEntity tableToSave);
}
