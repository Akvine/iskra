package ru.akvine.iskra.services;

import ru.akvine.iskra.services.domain.ColumnConfigurationModel;
import ru.akvine.iskra.services.dto.configuration.CreateColumnConfiguration;

import java.util.List;

public interface ColumnConfigurationService {
    List<ColumnConfigurationModel> list(String columnUuid);

    ColumnConfigurationModel create(CreateColumnConfiguration action);
}
