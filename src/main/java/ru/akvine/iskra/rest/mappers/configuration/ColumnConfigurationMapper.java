package ru.akvine.iskra.rest.mappers.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.components.SecurityManager;
import ru.akvine.iskra.rest.dto.configuration.column.ConfigurationDto;
import ru.akvine.iskra.rest.dto.configuration.column.ConfigurationListResponse;
import ru.akvine.iskra.rest.dto.configuration.column.CreateConfigurationRequest;
import ru.akvine.iskra.rest.dto.configuration.column.SelectConfigurationRequest;
import ru.akvine.iskra.services.domain.column.configuration.ColumnConfigurationModel;
import ru.akvine.iskra.services.domain.column.configuration.dictionary.ColumnConfigurationDictionaryModel;
import ru.akvine.iskra.services.domain.column.configuration.dto.CreateColumnConfiguration;
import ru.akvine.iskra.services.domain.column.configuration.dto.SelectColumnConfiguration;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ColumnConfigurationMapper {
    private final SecurityManager securityManager;

    public CreateColumnConfiguration mapToCreateColumnConfiguration(CreateConfigurationRequest request) {
        Asserts.isNotNull(request);

        return new CreateColumnConfiguration()
                .setColumnUuid(request.getColumnUuid())
                .setName(request.getName())
                .setType(request.getType())
                .setGenerationStrategy(request.getGenerationStrategy())
                .setRangeType(request.getRangeType())
                .setSelected(request.isSelected())
                .setConvertToString(request.isConvertToString())
                .setRepeatable(request.isRepeatable())
                .setDictionariesUuids(request.getDictionariesUuids())
                .setUnique(request.isUnique())
                .setNotNull(request.isNotNull())
                .setValid(request.getValid())
                .setRegexps(request.getRegexps().stream().toList())
                .setStart(request.getStart())
                .setEnd(request.getEnd())
                .setStep(request.getStep())
                .setConverters(request.getConverters())
                .setPostConverters(request.getPostConverters())
                .setUserUuid(securityManager.getCurrentUser().getUuid());
    }

    public ConfigurationListResponse mapToConfigurationListResponse(List<ColumnConfigurationModel> configs) {
        Asserts.isNotNull(configs);
        return new ConfigurationListResponse()
                .setCount(configs.size())
                .setConfigurations(configs.stream().map(this::buildConfigurationDto).toList());
    }

    public SelectColumnConfiguration mapToSelectColumnConfiguration(SelectConfigurationRequest request) {
        Asserts.isNotNull(request);
        return new SelectColumnConfiguration()
                .setColumnUuid(request.getColumnUuid())
                .setName(request.getName());
    }

    private ConfigurationDto buildConfigurationDto(ColumnConfigurationModel model) {
        ConfigurationDto config = new ConfigurationDto()
                .setName(model.getName())
                .setRepeatable(model.isRepeatable())
                .setConvertToString(model.isConvertToString())
                .setSelected(model.isSelected())
                .setType(model.getType())
                .setGenerationStrategy(model.getGenerationStrategy())
                .setUnique(model.isUnique())
                .setNotNull(model.isNotNull())
                .setRangeType(model.getRangeType())
                .setStart(model.getStart())
                .setEnd(model.getEnd())
                .setStep(model.getStep())
                .setValid(model.getValid())
                .setRegexps(model.getRegexps());

        if (CollectionUtils.isNotEmpty(model.getDictionaries())) {
            config.setDictionariesNames(model.getDictionaries().stream()
                    .map(ColumnConfigurationDictionaryModel::getDictionaryName).toList());
        }

        if (CollectionUtils.isNotEmpty(model.getConverters())) {
            config.setConverters(model.getConverters());
        }
        if (CollectionUtils.isNotEmpty(model.getPostConverters())) {
            config.setPostConverters(model.getPostConverters());
        }

        return config;
    }
}
