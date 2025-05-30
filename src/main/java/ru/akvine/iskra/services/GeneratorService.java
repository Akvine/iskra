package ru.akvine.iskra.services;

import ru.akvine.iskra.services.domain.table.TableModel;

public interface GeneratorService {
    boolean generate(String planUuid, TableModel table);
}
