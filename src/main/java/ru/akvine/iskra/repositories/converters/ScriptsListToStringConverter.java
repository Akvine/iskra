package ru.akvine.iskra.repositories.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Converter
public class ScriptsListToStringConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (CollectionUtils.isEmpty(attribute)) {
            return StringUtils.EMPTY;
        }
        return String.join(";", attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (StringUtils.isBlank(dbData)) {
            return List.of();
        }

        return Arrays.stream(dbData.split(";")).toList();
    }
}
