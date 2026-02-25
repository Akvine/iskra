package ru.akvine.iskra.repositories.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.List;
import ru.akvine.compozit.commons.istochnik.ConverterDto;
import ru.akvine.iskra.utils.ObjectMapperUtils;

@Converter
public class ConvertersToStringConverter implements AttributeConverter<List<ConverterDto>, String> {
    @Override
    public String convertToDatabaseColumn(List<ConverterDto> attribute) {
        return ObjectMapperUtils.writeValueAsString(new ObjectMapper(), attribute);
    }

    @Override
    public List<ConverterDto> convertToEntityAttribute(String filters) {
        return ObjectMapperUtils.safeReadValue(filters, List.class, new ObjectMapper());
    }
}
