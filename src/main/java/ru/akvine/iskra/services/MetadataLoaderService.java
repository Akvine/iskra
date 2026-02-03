package ru.akvine.iskra.services;

import org.springframework.transaction.annotation.Transactional;
import ru.akvine.iskra.services.domain.table.TableModel;

import java.util.List;

public interface MetadataLoaderService {
    @Transactional
    // TODO: обращение к внешнему сервису под Transactional может привести к исчерпанию коннектов к БД
    List<TableModel> loadOrList(String planUuid, String userUuid);
}
