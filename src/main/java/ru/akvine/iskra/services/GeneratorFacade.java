package ru.akvine.iskra.services;

import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.services.domain.table.TableModel;

import java.util.Map;

public interface GeneratorFacade {
    String generate(String planUuid,
                    Map<TableName, TableModel> selectedTables,
                    boolean resume);
}
