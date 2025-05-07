package ru.akvine.iskra.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import ru.akvine.iskra.exceptions.UnsupportedTypeException;

@Getter
@AllArgsConstructor
public enum DatabaseType {
    POSTGRESQL("postgresql");

    private final String value;

    public static DatabaseType from(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("Value can't be blank!");
        }

        for (DatabaseType type : values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }

        throw new UnsupportedTypeException("Unsupported database type = [" + value + "] is not supported by app!");
    }
}
