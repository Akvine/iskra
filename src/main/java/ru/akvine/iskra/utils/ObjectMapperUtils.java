package ru.akvine.iskra.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.exceptions.MarshalingException;

@UtilityClass
public class ObjectMapperUtils {
    public String writeValueAsString(ObjectMapper instance, Object value) {
        Asserts.isNotNull(instance);
        Asserts.isNotNull(value);

        try {
            return instance.writeValueAsString(value);
        } catch (JsonProcessingException exception) {
            throw new MarshalingException(exception.getMessage());
        }
    }

    public <T> T readValue(String content, Class<T> valueType, ObjectMapper instance) {
        Asserts.isNotNull(instance);
        Asserts.isNotNull(valueType);

        if (StringUtils.isBlank(content)) {
            throw new MarshalingException("Content is blank");
        }

        try {
            return instance.readValue(content, valueType);
        } catch (JsonProcessingException exception) {
            String message = String.format(
                    "Some error was occurred while marshaling json to class with type = [%s]. Message = [%s]",
                    valueType.getSimpleName(),
                    exception.getMessage()
            );
            throw new MarshalingException(message);
        }
    }
}
