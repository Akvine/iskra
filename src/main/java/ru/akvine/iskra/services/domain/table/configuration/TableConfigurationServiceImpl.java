package ru.akvine.iskra.services.domain.table.configuration;

import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.exceptions.table.configuration.TableConfigurationNotFoundException;
import ru.akvine.iskra.repositories.TableConfigurationRepository;
import ru.akvine.iskra.repositories.TableRepository;
import ru.akvine.iskra.repositories.entities.TableEntity;
import ru.akvine.iskra.repositories.entities.config.TableConfigurationEntity;
import ru.akvine.iskra.services.domain.connection.ConnectionModel;
import ru.akvine.iskra.services.domain.table.TableService;
import ru.akvine.iskra.services.domain.table.configuration.dto.CreateTableConfiguration;
import ru.akvine.iskra.services.domain.table.configuration.dto.UpdateTableConfiguration;
import ru.akvine.iskra.services.integration.visor.VisorService;

@Service
@RequiredArgsConstructor
public class TableConfigurationServiceImpl implements TableConfigurationService {
    private final TableConfigurationRepository tableConfigurationRepository;
    private final TableRepository tableRepository;

    private final TableService tableService;
    private final VisorService visorService;

    @Override
    public List<TableConfigurationModel> list(String planUuid, Collection<String> tableNames) {
        Asserts.isNotBlank(planUuid);
        Asserts.isNotEmpty(tableNames);
        return tableConfigurationRepository.findBy(planUuid, tableNames).stream()
                .map(TableConfigurationModel::new)
                .toList();
    }

    @Override
    public TableConfigurationModel create(CreateTableConfiguration action) {
        Asserts.isNotNull(action);

        TableEntity table = tableService.verifyExistsBy(action.getPlanUuid(), action.getTableName());
        TableConfigurationEntity configurationToSave = new TableConfigurationEntity()
                .setName(action.getName())
                .setRowsCount(action.getRowsCount())
                .setBatchSize(action.getBatchSize())
                .setTable(table)
                .setDeleteMode(action.getDeleteMode());

        String clearScript;
        Boolean generateClearScript = action.getGenerateClearScript();
        if (generateClearScript != null && generateClearScript.equals(Boolean.TRUE)) {
            ConnectionModel connection = new ConnectionModel(table.getPlan().getConnection());
            clearScript = visorService.generateClearScript(action.getTableName(), action.getDeleteMode(), connection);
            configurationToSave.setClearScripts(clearScript);
        }

        TableConfigurationEntity savedConfiguration = tableConfigurationRepository.save(configurationToSave);
        table.setConfiguration(savedConfiguration);
        tableRepository.save(table);

        return new TableConfigurationModel(savedConfiguration);
    }

    @Override
    public TableConfigurationModel update(UpdateTableConfiguration action) {
        Asserts.isNotNull(action);

        TableEntity table = tableService.verifyExistsBy(action.getPlanUuid(), action.getTableName());
        TableConfigurationEntity configurationToUpdate = verifyExistsByName(action.getTableName());

        if (StringUtils.isNotBlank(action.getName()) && !action.getName().equals(configurationToUpdate.getName())) {
            configurationToUpdate.setName(action.getName());
        }

        if (action.getRowsCount() != null && !action.getRowsCount().equals(configurationToUpdate.getRowsCount())) {
            configurationToUpdate.setRowsCount(action.getRowsCount());
        }

        if (action.getBatchSize() != null && !action.getBatchSize().equals(configurationToUpdate.getBatchSize())) {
            configurationToUpdate.setBatchSize(action.getBatchSize());
        }

        if (action.getDeleteMode() != null && !action.getDeleteMode().equals(configurationToUpdate.getDeleteMode())) {
            configurationToUpdate.setDeleteMode(action.getDeleteMode());
        }

        if (action.getDeleteMode() != null && action.getGenerateClearScript() != null) {
            String clearScript = visorService.generateClearScript(
                    action.getTableName(),
                    action.getDeleteMode(),
                    new ConnectionModel(table.getPlan().getConnection()));
            configurationToUpdate.setClearScripts(clearScript);
        }

        if (CollectionUtils.isNotEmpty(action.getDropScripts())) {
            configurationToUpdate.setDropScripts(action.getDropScripts());
        }

        if (CollectionUtils.isNotEmpty(action.getCreateScripts())) {
            configurationToUpdate.setCreateScripts(action.getCreateScripts());
        }

        return new TableConfigurationModel(tableConfigurationRepository.save(configurationToUpdate));
    }

    @Override
    public TableConfigurationEntity verifyExistsByName(String tableName) {
        Asserts.isNotNull(tableName);
        return tableConfigurationRepository.findBy(tableName).orElseThrow(() -> {
            String errorMessage = String.format("Table configuration for table with name = [%s] not found!", tableName);
            return new TableConfigurationNotFoundException(errorMessage);
        });
    }
}
