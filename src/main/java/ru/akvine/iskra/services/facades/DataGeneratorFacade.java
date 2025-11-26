package ru.akvine.iskra.services.facades;

import ru.akvine.iskra.services.domain.table.TableModel;

public interface DataGeneratorFacade {
    void generate(String processUuid, TableModel table, boolean resume);
}
