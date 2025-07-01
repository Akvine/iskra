package ru.akvine.iskra.services.domain.column.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.exceptions.column.configuration.ConfigurationAlreadyExistsException;
import ru.akvine.iskra.exceptions.column.configuration.ConfigurationMaxCountException;
import ru.akvine.iskra.exceptions.column.configuration.ConfigurationNotFoundException;
import ru.akvine.iskra.repositories.ColumnConfigurationRepository;
import ru.akvine.iskra.repositories.entities.ColumnEntity;
import ru.akvine.iskra.repositories.entities.DictionaryEntity;
import ru.akvine.iskra.repositories.entities.config.ColumnConfigurationEntity;
import ru.akvine.iskra.services.domain.column.ColumnService;
import ru.akvine.iskra.services.domain.column.configuration.dictionary.ColumnConfigurationDictionaryService;
import ru.akvine.iskra.services.domain.dictionary.DictionaryService;
import ru.akvine.iskra.services.domain.column.configuration.dto.CreateColumnConfiguration;
import ru.akvine.iskra.services.domain.column.configuration.dto.SelectColumnConfiguration;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ColumnConfigurationServiceImpl implements ColumnConfigurationService {
    private final ColumnConfigurationRepository columnConfigurationRepository;

    @Value("${max.configs.per.column}")
    private int maxConfigsPerColumn;

    private final ColumnService columnService;
    private final DictionaryService dictionaryService;
    private final ColumnConfigurationDictionaryService columnConfigurationDictionaryService;

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
                    .setConverters(action.getConverters())
                    .setPostConverters(action.getPostConverters())
                    .setRegexps(String.join(";", action.getRegexps()))
                    .setColumn(column);

            Set<String> dictionaryUuids = action.getDictionariesUuids();
            if (CollectionUtils.isNotEmpty(dictionaryUuids)) {
                List<DictionaryEntity> userDictionaries;
                List<DictionaryEntity> systemDictionaries;

                systemDictionaries = dictionaryService.verifySystemExists(dictionaryUuids);
                userDictionaries = dictionaryService.verifyUserExists(dictionaryUuids, action.getUserUuid());

                ColumnConfigurationEntity createdColumnConfigurationEntity = columnConfigurationRepository.save(columnConfigurationToCreate);
                columnConfigurationDictionaryService.create(createdColumnConfigurationEntity, ListUtils.union(userDictionaries, systemDictionaries));

                return new ColumnConfigurationModel(createdColumnConfigurationEntity);
            } else {
                return new ColumnConfigurationModel(columnConfigurationRepository.save(columnConfigurationToCreate));
            }
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
