package ru.akvine.iskra.services.domain.table;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.exceptions.table.TableNotFoundException;
import ru.akvine.iskra.repositories.TableRepository;
import ru.akvine.iskra.repositories.entities.TableEntity;
import ru.akvine.iskra.services.dto.table.ListTables;
import ru.akvine.iskra.services.dto.table.ToogleSelectedTables;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Override
    public TableEntity verifyExistsBy(String planUuid, String name) {
        Asserts.isNotNull(name);
        Asserts.isNotNull(planUuid);
        return tableRepository.findBy(planUuid, name)
                .orElseThrow(() -> {
                    String message = "Table by name = [" + name + "] not found for plan = [" + planUuid + "]";
                    return new TableNotFoundException(message);
                });
    }

    @Override
    public List<TableModel> list(ListTables listTables) {
        Asserts.isNotNull(listTables);
        return tableRepository.findAll(listTables.getPlanUuid(), listTables.isSelected())
                .stream()
                .map(TableModel::new)
                .toList();
    }

    @Override
    public List<TableModel> toggleSelected(ToogleSelectedTables action) {
        Asserts.isNotNull(action);

        Map<String, Boolean> toggled = action.getToggled();
        Map<String, TableEntity> tablesToUpdate = map(tableRepository.findAll(
                action.getPlanUuid(),
                action.getToggled().keySet(),
                action.getUserUuid()
        ));

        for (String tableName : tablesToUpdate.keySet()) {
            Boolean isSelect = toggled.get(tableName);
            TableEntity tableToUpdate = tablesToUpdate.get(tableName);

            if (isSelect != null && tableToUpdate != null) {
                tableToUpdate.setSelected(isSelect);
            }
        }

        return tableRepository.saveAll(tablesToUpdate.values()).stream()
                .map(TableModel::new)
                .toList();
    }


    private Map<String, TableEntity> map(List<TableEntity> tablesToUpdate) {
        return tablesToUpdate.stream()
                .collect(Collectors.toMap(
                        TableEntity::getName,
                        Function.identity()
                ));
    }
}
