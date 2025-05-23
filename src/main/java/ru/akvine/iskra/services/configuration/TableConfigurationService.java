package ru.akvine.iskra.services.configuration;

import ru.akvine.iskra.services.domain.configuration.TableConfigurationModel;
import ru.akvine.iskra.services.dto.configuration.table.CreateTableConfiguration;

public interface TableConfigurationService {
    TableConfigurationModel create(CreateTableConfiguration action);
}
