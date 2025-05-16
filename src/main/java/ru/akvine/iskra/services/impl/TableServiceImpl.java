package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.repositories.TableRepository;
import ru.akvine.iskra.repositories.entities.TableEntity;
import ru.akvine.iskra.services.TableService;
import ru.akvine.iskra.services.domain.TableModel;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {
    private final TableRepository tableRepository;

    @Override
    public List<TableModel> getAll(String planUuid) {
        Asserts.isNotNull(planUuid);
        return tableRepository.findAll(planUuid).stream()
                .map(TableModel::new)
                .toList();
    }

    @Override
    public TableEntity save(TableEntity tableToSave) {
        Asserts.isNotNull(tableToSave);
        return tableRepository.save(tableToSave);
    }
}
