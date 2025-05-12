package ru.akvine.iskra.services;

import ru.akvine.iskra.services.dto.table.TableModel;

import java.util.List;

public interface TableService {
    List<TableModel> createOrListIfExist(String planUuid);
}
