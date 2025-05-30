package ru.akvine.iskra.services.domain.column.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.exceptions.column.configuration.ConfigurationAlreadyExistsException;
import ru.akvine.iskra.exceptions.column.configuration.ConfigurationMaxCountException;
import ru.akvine.iskra.exceptions.column.configuration.ConfigurationNotFoundException;
import ru.akvine.iskra.repositories.ColumnConfigurationRepository;
import ru.akvine.iskra.repositories.entities.config.ColumnConfigurationEntity;
import ru.akvine.iskra.repositories.entities.ColumnEntity;
import ru.akvine.iskra.repositories.entities.DictionaryEntity;
import ru.akvine.iskra.services.domain.column.ColumnService;
import ru.akvine.iskra.services.domain.dictionary.DictionaryService;
import ru.akvine.iskra.services.dto.configuration.column.CreateColumnConfiguration;
import ru.akvine.iskra.services.dto.configuration.column.SelectColumnConfiguration;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColumnConfigurationServiceImpl implements ColumnConfigurationService {
    private final ColumnConfigurationRepository columnConfigurationRepository;

    @Value("${max.configs.per.column}")
    private int maxConfigsPerColumn;

    private final ColumnService columnService;
    private final DictionaryService dictionaryService;

    @Override
    public List<ColumnConfigurationModel> list(String columnUuid) {
        Asserts.isNotNull(columnUuid);
        return columnConfigurationRepository
                .findAll(columnUuid).stream()
                .map(ColumnConfigurationModel::new)
                .toList();
    }

    @Override
    public ColumnConfigurationModel create(CreateColumnConfiguration action) {
        Asserts.isNotNull(action);

        ColumnEntity column = columnService.verifyExists(action.getColumnUuid());

        if (columnConfigurationRepository.count(column.getUuid()) == maxConfigsPerColumn) {
            String message = "Max configurations count [" + maxConfigsPerColumn + "] was exceeded! Remove unnecessary configurations";
            throw new ConfigurationMaxCountException(message);
        }

        try {
            verifyExistsBy(action.getColumnUuid(), action.getName());
            String message = "Config with name = [" + action.getName() + "] already exists!";
            throw new ConfigurationAlreadyExistsException(message);
        } catch (ConfigurationNotFoundException exception) {
            ColumnConfigurationEntity columnConfigurationToCreate = new ColumnConfigurationEntity()
                    .setSelected(action.isSelected())
                    .setName(action.getName())
                    .setType(action.getType())
                    .setRepeatable(action.isRepeatable())
                    .setGenerationStrategy(action.getGenerationStrategy())
                    .setUnique(action.isUnique())
                    .setNotNull(action.isNotNull())
                    .setRangeType(action.getRangeType())
                    .setStart(action.getStart())
                    .setEnd(action.getEnd())
                    .setStep(action.getStep())
                    .setValid(action.getValid())
                    .setConvertToString(action.isConvertToString())
                    .setFilters(action.getFilters())
                    .setPostFilters(action.getPostFilters())
                    .setRegexps(String.join(";", action.getRegexps()))
                    .setColumn(column);

            if (StringUtils.isNotBlank(action.getDictionaryName())) {
                DictionaryEntity dictionary = dictionaryService.verifyExists(action.getDictionaryName());
                columnConfigurationToCreate.setDictionary(dictionary);
            }

            return new ColumnConfigurationModel(columnConfigurationRepository.save(columnConfigurationToCreate));
        }
    }

    @Override
    public List<ColumnConfigurationModel> select(SelectColumnConfiguration action) {
        Asserts.isNotNull(action);

        columnService.verifyExists(action.getColumnUuid());
        verifyExistsBy(action.getColumnUuid(), action.getName());

        List<ColumnConfigurationEntity> configs = columnConfigurationRepository.findAll(action.getColumnUuid());
        for (ColumnConfigurationEntity configEntity : configs) {
            configEntity.setSelected(configEntity.getName().equals(action.getName()));
        }

        return columnConfigurationRepository.saveAll(configs).stream()
                .map(ColumnConfigurationModel::new)
                .toList();
    }

    @Override
    public ColumnConfigurationEntity verifyExistsBy(String columnUuid, String name) {
        Asserts.isNotNull(columnUuid);
        Asserts.isNotNull(name);

        return columnConfigurationRepository
                .findBy(columnUuid, name)
                .orElseThrow(() -> {
                    String message = String.format(
                            "Column config not found by column uuid = [%s] and name = [%s]",
                            columnUuid, name
                    );
                    return new ConfigurationNotFoundException(message);
                });
    }
}
