package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.exceptions.column.ColumnNotFoundException;
import ru.akvine.iskra.repositories.ColumnRepository;
import ru.akvine.iskra.repositories.entities.ColumnEntity;
import ru.akvine.iskra.services.ColumnService;
import ru.akvine.iskra.services.TableService;
import ru.akvine.iskra.services.domain.ColumnModel;
import ru.akvine.iskra.services.dto.column.SelectColumn;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColumnServiceImpl implements ColumnService {
    private final ColumnRepository columnRepository;

    private final TableService tableService;

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
    public List<ColumnModel> selectAll(SelectColumn selectColumn) {
        Asserts.isNotNull(selectColumn);

        String tableName = selectColumn.getTableName();
        tableService.verifyExistsByName(tableName);

        Map<String, Boolean> selected = selectColumn.getSelected();
        Map<String, ColumnEntity> columnsToUpdate = map(columnRepository.findAll(
                tableName,
                selectColumn.getSelected().keySet()
        ));

        for (String columnUuid : selected.keySet()) {
            Boolean isSelect = selected.get(columnUuid);
            ColumnEntity columnToUpdate = columnsToUpdate.get(columnUuid);

            if (isSelect != null && columnToUpdate != null) {
                columnToUpdate.setSelected(isSelect);
            }
        }

        return columnRepository.saveAll(columnsToUpdate.values()).stream()
                .map(ColumnModel::new)
                .toList();
    }

    @Override
    public List<ColumnModel> saveAll(Collection<ColumnEntity> columnsToSave) {
        return columnRepository.saveAll(columnsToSave).stream()
                .map(ColumnModel::new)
                .toList();
    }

    private Map<String, ColumnEntity> map(List<ColumnEntity> columnsToUpdate) {
        return columnsToUpdate.stream()
                .collect(Collectors.toMap(
                        ColumnEntity::getUuid,
                        Function.identity()
                ));
    }
}
