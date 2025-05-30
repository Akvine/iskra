package ru.akvine.iskra.rest.converter.configuration;

import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.rest.dto.configuration.column.ConfigurationDto;
import ru.akvine.iskra.rest.dto.configuration.column.ConfigurationListResponse;
import ru.akvine.iskra.rest.dto.configuration.column.CreateConfigurationRequest;
import ru.akvine.iskra.rest.dto.configuration.column.SelectConfigurationRequest;
import ru.akvine.iskra.services.domain.column.configuration.ColumnConfigurationModel;
import ru.akvine.iskra.services.dto.configuration.column.CreateColumnConfiguration;
import ru.akvine.iskra.services.dto.configuration.column.SelectColumnConfiguration;

import java.util.List;

@Component
public class ColumnConfigurationConverter {
    public CreateColumnConfiguration convertToCreateColumnConfiguration(CreateConfigurationRequest request) {
        Asserts.isNotNull(request);

        return new CreateColumnConfiguration()
                .setColumnUuid(request.getColumnUuid())
                .setName(request.getName())
                .setType(request.getType())
                .setGenerationStrategy(request.getGenerationStrategy())
                .setSelected(request.isSelected())
                .setRepeatable(request.isRepeatable())
                .setDictionaryName(request.getDictionaryName())
                .setUnique(request.isUnique())
                .setNotNull(request.isNotNull())
                .setValid(request.getValid())
                .setRegexps(request.getRegexps().stream().toList())
                .setStart(request.getStart())
                .setEnd(request.getEnd())
                .setStep(request.getStep());
    }

    public ConfigurationListResponse convertToConfigurationListResponse(List<ColumnConfigurationModel> configs) {
        Asserts.isNotNull(configs);
        return new ConfigurationListResponse()
                .setCount(configs.size())
                .setConfigurations(configs.stream().map(this::buildConfigurationDto).toList());
    }

    public SelectColumnConfiguration convertToSelectColumnConfiguration(SelectConfigurationRequest request) {
        Asserts.isNotNull(request);
        return new SelectColumnConfiguration()
                .setColumnUuid(request.getColumnUuid())
                .setName(request.getName());
    }

    private ConfigurationDto buildConfigurationDto(ColumnConfigurationModel model) {
        ConfigurationDto config = new ConfigurationDto()
                .setName(model.getName())
                .setRepeatable(model.isRepeatable())
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

        if (model.getDictionary() != null) {
            config.setDictionaryName(model.getDictionary().getName());
        }

        return config;
    }
}
