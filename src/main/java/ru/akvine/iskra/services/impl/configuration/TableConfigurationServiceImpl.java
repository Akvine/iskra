package ru.akvine.iskra.services.impl.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.repositories.TableConfigurationRepository;
import ru.akvine.iskra.repositories.TableRepository;
import ru.akvine.iskra.repositories.entities.TableEntity;
import ru.akvine.iskra.repositories.entities.config.TableConfigurationEntity;
import ru.akvine.iskra.services.TableService;
import ru.akvine.iskra.services.configuration.TableConfigurationService;
import ru.akvine.iskra.services.domain.configuration.TableConfigurationModel;
import ru.akvine.iskra.services.dto.configuration.table.CreateTableConfiguration;

@Service
@RequiredArgsConstructor
public class TableConfigurationServiceImpl implements TableConfigurationService {
    private final TableConfigurationRepository tableConfigurationRepository;
    private final TableRepository tableRepository;

    private final TableService tableService;

    @Override
    public TableConfigurationModel create(CreateTableConfiguration action) {
        Asserts.isNotNull(action);

        TableEntity table = tableService.verifyExistsByName(action.getTableName());

        TableConfigurationEntity configurationToSave = new TableConfigurationEntity()
                .setName(action.getName())
                .setRowsCount(action.getRowsCount())
                .setBatchSize(action.getBatchSize())
                .setTable(table);
        TableConfigurationEntity savedConfiguration = tableConfigurationRepository.save(configurationToSave);
        table.setConfiguration(savedConfiguration);
        tableRepository.save(table);

        return new TableConfigurationModel(savedConfiguration);
    }
}
