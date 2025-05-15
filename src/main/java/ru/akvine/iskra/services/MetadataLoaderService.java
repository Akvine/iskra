package ru.akvine.iskra.services;

import org.springframework.transaction.annotation.Transactional;
import ru.akvine.iskra.services.dto.table.TableModel;

import java.util.List;

public interface MetadataLoaderService {
    @Transactional
    List<TableModel> loadOrList(String planUuid);
}
