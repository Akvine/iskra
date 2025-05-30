package ru.akvine.iskra.repositories.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.akvine.compozit.commons.istochnik.FilterDto;
import ru.akvine.iskra.utils.ObjectMapperUtils;

import java.util.List;

@Converter
public class FiltersToStringConverter implements AttributeConverter<List<FilterDto>, String> {
    @Override
    public String convertToDatabaseColumn(List<FilterDto> attribute) {
        return ObjectMapperUtils.writeValueAsString(new ObjectMapper(), attribute);
    }

    @Override
    public List<FilterDto> convertToEntityAttribute(String filters) {
        return ObjectMapperUtils.readValue(filters, List.class, new ObjectMapper());
    }
}
