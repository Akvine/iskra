package ru.akvine.iskra.repositories.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import ru.akvine.iskra.enums.ConstraintType;

@Converter
public class ConstraintListToStringConverter implements AttributeConverter<List<ConstraintType>, String> {
    private static final String SEPARATOR = ",";

    @Override
    public String convertToDatabaseColumn(List<ConstraintType> attribute) {
        if (CollectionUtils.isEmpty(attribute)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        int lastElementIndex = attribute.size() - 1;
        for (int i = 0; i < attribute.size(); ++i) {
            sb.append(attribute.get(i).getName());
            if (i != lastElementIndex) {
                sb.append(SEPARATOR);
            }
        }
        return sb.toString();
    }

    @Override
    public List<ConstraintType> convertToEntityAttribute(String dbData) {
        if (StringUtils.isBlank(dbData)) {
            return null;
        }

        return Arrays.stream(dbData.split(SEPARATOR))
                .map(ConstraintType::fromName)
                .toList();
    }
}
