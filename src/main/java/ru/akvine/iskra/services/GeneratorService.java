package ru.akvine.iskra.services;

import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.domain.table.process.TableProcessModel;

public interface GeneratorService {
    TableProcessModel generate(String processUuid, TableModel table);
}
