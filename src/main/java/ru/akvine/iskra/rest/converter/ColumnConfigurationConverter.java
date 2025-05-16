package ru.akvine.iskra.rest.converter;

import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.rest.dto.configuration.ConfigurationDto;
import ru.akvine.iskra.rest.dto.configuration.ConfigurationListResponse;
import ru.akvine.iskra.rest.dto.configuration.CreateConfigurationRequest;
import ru.akvine.iskra.services.domain.ColumnConfigurationModel;
import ru.akvine.iskra.services.dto.configuration.CreateColumnConfiguration;

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

    private ConfigurationDto buildConfigurationDto(ColumnConfigurationModel model) {
        ConfigurationDto config = new ConfigurationDto()
                .setName(model.getName())
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
