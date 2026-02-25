package ru.akvine.iskra.services.domain.column.configuration;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.enums.RelationShipType;
import ru.akvine.iskra.exceptions.column.configuration.ConfigurationAlreadyExistsException;
import ru.akvine.iskra.exceptions.column.configuration.ConfigurationMaxCountException;
import ru.akvine.iskra.exceptions.column.configuration.ConfigurationNotFoundException;
import ru.akvine.iskra.repositories.ColumnConfigurationRepository;
import ru.akvine.iskra.repositories.entities.ColumnEntity;
import ru.akvine.iskra.repositories.entities.DictionaryEntity;
import ru.akvine.iskra.repositories.entities.config.ColumnConfigurationEntity;
import ru.akvine.iskra.services.domain.column.ColumnModel;
import ru.akvine.iskra.services.domain.column.ColumnService;
import ru.akvine.iskra.services.domain.column.configuration.dictionary.ColumnConfigurationDictionaryService;
import ru.akvine.iskra.services.domain.column.configuration.dto.CreateColumnConfiguration;
import ru.akvine.iskra.services.domain.column.configuration.dto.SelectColumnConfiguration;
import ru.akvine.iskra.services.domain.dictionary.DictionaryService;

@Service
@RequiredArgsConstructor
public class ColumnConfigurationServiceImpl implements ColumnConfigurationService {
    private static final Logger log = LoggerFactory.getLogger(ColumnConfigurationServiceImpl.class);
    private final ColumnConfigurationRepository columnConfigurationRepository;

    @Value("${max.configs.per.column}")
    private int maxConfigsPerColumn;

    private final ColumnService columnService;
    private final DictionaryService dictionaryService;
    private final ColumnConfigurationDictionaryService columnConfigurationDictionaryService;

    @Override
    public List<ColumnConfigurationModel> list(String columnUuid) {
        Asserts.isNotNull(columnUuid);
        return columnConfigurationRepository.findAll(columnUuid).stream()
                .map(ColumnConfigurationModel::new)
                .toList();
    }

    @Override
    public ColumnConfigurationModel create(CreateColumnConfiguration action) {
        Asserts.isNotNull(action);

        ColumnEntity column = columnService.verifyExists(action.getColumnUuid());

        if (columnConfigurationRepository.count(column.getUuid()) == maxConfigsPerColumn) {
            String message = "Max configurations count ["
                    + maxConfigsPerColumn
                    + "] was exceeded! Remove unnecessary configurations";
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

                ColumnConfigurationEntity createdColumnConfigurationEntity =
                        columnConfigurationRepository.save(columnConfigurationToCreate);
                columnConfigurationDictionaryService.create(
                        createdColumnConfigurationEntity, ListUtils.union(userDictionaries, systemDictionaries));

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
    public ColumnConfigurationEntity getSelected(String planUuid, String tableName, String columnName) {
        Asserts.isNotNull(columnName, "columnName is null");
        Asserts.isNotNull(planUuid, "planUuid is null");
        Asserts.isNotNull(tableName, "tableName is null");

        Optional<ColumnConfigurationEntity> configuration =
                columnConfigurationRepository.findSelected(columnName, tableName, planUuid);
        if (configuration.isEmpty()) {
            throw new ConfigurationNotFoundException(
                    "Selected column configuration for column uuid = [" + columnName + "] not found!");
        }

        return configuration.get();
    }

    @Override
    public ColumnConfigurationEntity verifyExistsBy(String columnUuid, String name) {
        Asserts.isNotNull(columnUuid);
        Asserts.isNotNull(name);

        return columnConfigurationRepository.findBy(columnUuid, name).orElseThrow(() -> {
            String message =
                    String.format("Column config not found by column uuid = [%s] and name = [%s]", columnUuid, name);
            return new ConfigurationNotFoundException(message);
        });
    }

    @Override
    public List<ColumnConfigurationModel> generateForExternalRelations(String planUuid) {
        Asserts.isNotNull(planUuid, "planUuid is null");
        List<ColumnModel> referenceColumns = columnService.getWithReferenceInfo(planUuid);
        List<ColumnConfigurationModel> configs = columnConfigurationRepository
                .findByColumnsUuids(
                        referenceColumns.stream().map(ColumnModel::getUuid).collect(Collectors.toSet()))
                .stream()
                .map(ColumnConfigurationModel::new)
                .toList();

        List<ColumnConfigurationModel> generatedConfigs = new ArrayList<>();
        Collection<ColumnModel> columnsWithoutConfigs = getColumnUuidsWithoutConfigs(referenceColumns, configs);
        for (ColumnModel referenceColumnWithoutConfig : columnsWithoutConfigs) {
            try {
                ColumnConfigurationEntity configuration = getSelected(
                        planUuid,
                        referenceColumnWithoutConfig.getTargetTableNameForForeignKey(),
                        referenceColumnWithoutConfig.getTargetColumnNameForForeignKey());
                ColumnConfigurationEntity targetCopyConfig = new ColumnConfigurationEntity()
                        .setSelected(configuration.isSelected())
                        .setName(configuration.getName())
                        .setType(configuration.getType())
                        .setRepeatable(
                                referenceColumnWithoutConfig.getRelationShipType() == RelationShipType.ONE_TO_MANY)
                        .setGenerationStrategy(configuration.getGenerationStrategy())
                        .setUnique(configuration.isUnique())
                        .setNotNull(configuration.isNotNull())
                        .setRangeType(configuration.getRangeType())
                        .setStart(configuration.getStart())
                        .setEnd(configuration.getEnd())
                        .setStep(configuration.getStep())
                        .setValid(configuration.getValid())
                        .setConvertToString(configuration.isConvertToString())
                        .setConverters(configuration.getConverters())
                        .setPostConverters(configuration.getPostConverters())
                        .setRegexps(String.join(";", configuration.getRegexps()))
                        .setColumn(configuration.getColumn());

                generatedConfigs.add(
                        new ColumnConfigurationModel(columnConfigurationRepository.save(targetCopyConfig)));
            } catch (ConfigurationNotFoundException exception) {
                log.info(
                        "Selected configuration for column with name = [{}] for table = [{}] not found",
                        referenceColumnWithoutConfig.getTargetColumnNameForForeignKey(),
                        referenceColumnWithoutConfig.getTargetTableNameForForeignKey());
            }
        }

        return generatedConfigs;
    }

    // TODO: очень сложно читать содержимое метода. Можно убрать метод ниже и просто
    //  подгружать колонки вместе с конфигурациями из базы и првоерять на их наличие
    private Collection<ColumnModel> getColumnUuidsWithoutConfigs(
            List<ColumnModel> columns, List<ColumnConfigurationModel> configs) {
        Set<String> difference = new HashSet<>();
        Map<String, ColumnModel> uuidsPerColumns =
                columns.stream().collect(Collectors.toMap(ColumnModel::getUuid, Function.identity()));
        Set<String> columnUuids = columns.stream().map(ColumnModel::getUuid).collect(Collectors.toSet());
        Set<String> columnUuidsWithConfigs =
                configs.stream().map(ColumnConfigurationModel::getColumnUuid).collect(Collectors.toSet());
        for (String uuid : columnUuids) {
            if (!columnUuidsWithConfigs.contains(uuid)) {
                difference.add(uuid);
            }
        }

        return uuidsPerColumns.entrySet().stream()
                .filter(entry -> difference.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                .values();
    }
}
