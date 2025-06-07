package ru.akvine.iskra.repositories.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.akvine.iskra.repositories.dto.RelationsMatrix;
import ru.akvine.iskra.utils.ObjectMapperUtils;

@Converter
public class RelationsMatrixToStringConverter implements AttributeConverter<RelationsMatrix, String> {
    @Override
    public String convertToDatabaseColumn(RelationsMatrix attribute) {
        if (attribute == null) {
            return null;
        }
        return ObjectMapperUtils.writeValueAsString(new ObjectMapper(), attribute);
    }

    @Override
    public RelationsMatrix convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return ObjectMapperUtils.safeReadValue(dbData, RelationsMatrix.class, new ObjectMapper());
    }
}
