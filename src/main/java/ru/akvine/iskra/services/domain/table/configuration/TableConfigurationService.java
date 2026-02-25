package ru.akvine.iskra.services.domain.table.configuration;

import java.util.Collection;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import ru.akvine.iskra.repositories.entities.config.TableConfigurationEntity;
import ru.akvine.iskra.services.domain.table.configuration.dto.CreateTableConfiguration;
import ru.akvine.iskra.services.domain.table.configuration.dto.UpdateTableConfiguration;

public interface TableConfigurationService {

    @Transactional
    List<TableConfigurationModel> list(String planUuid, Collection<String> tableNames);

    TableConfigurationModel create(CreateTableConfiguration action);

    TableConfigurationModel update(UpdateTableConfiguration action);

    TableConfigurationEntity verifyExistsByName(String tableName);
}
