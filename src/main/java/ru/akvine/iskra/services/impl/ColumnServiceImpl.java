package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.exceptions.column.ColumnNotFoundException;
import ru.akvine.iskra.repositories.ColumnRepository;
import ru.akvine.iskra.repositories.entities.ColumnEntity;
import ru.akvine.iskra.services.ColumnService;
import ru.akvine.iskra.services.domain.ColumnModel;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ColumnServiceImpl implements ColumnService {
    private final ColumnRepository columnRepository;

    @Override
    public List<ColumnModel> getAll(Collection<String> tableNames) {
        return columnRepository
                .findAll(tableNames).stream()
                .map(ColumnModel::new)
                .toList();
    }

    @Override
    public ColumnModel getByUuid(String uuid) {
        return new ColumnModel(verifyExists(uuid));
    }

    @Override
    public ColumnEntity verifyExists(String uuid) {
        Asserts.isNotNull(uuid);
        return columnRepository
                .findByUuid(uuid)
                .orElseThrow(() -> {
                    String errorMessage = "Column with uuid = [" + uuid + "] is not found!";
                    return new ColumnNotFoundException(errorMessage);
                });
    }

    @Override
    public List<ColumnModel> saveAll(Collection<ColumnEntity> columnsToSave) {
        return columnRepository.saveAll(columnsToSave).stream()
                .map(ColumnModel::new)
                .toList();
    }
}
