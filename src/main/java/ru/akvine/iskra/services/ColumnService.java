package ru.akvine.iskra.services;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.akvine.iskra.repositories.entities.ColumnEntity;
import ru.akvine.iskra.services.domain.ColumnModel;
import ru.akvine.iskra.services.dto.column.SelectColumn;

import java.util.Collection;
import java.util.List;

public interface ColumnService {
    @Transactional(readOnly = true)
    List<ColumnModel> getAll(Collection<String> tableNames);

    ColumnModel getByUuid(String uuid);

    ColumnEntity verifyExists(String uuid);

    List<ColumnModel> selectAll(SelectColumn selectColumn);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    List<ColumnModel> saveAll(Collection<ColumnEntity> columnsToSave);
}
