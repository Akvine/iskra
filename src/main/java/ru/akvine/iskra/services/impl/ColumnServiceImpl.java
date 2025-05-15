package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
    public List<ColumnModel> saveAll(Collection<ColumnEntity> columnsToSave) {
        return columnRepository.saveAll(columnsToSave).stream()
                .map(ColumnModel::new)
                .toList();
    }
}
