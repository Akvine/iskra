package ru.akvine.iskra.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import ru.akvine.iskra.exceptions.UnsupportedTypeException;

@Getter
@AllArgsConstructor
public enum Language {
    RU("ru");

    private final String name;

    public static Language from(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("Value is blank!");
        }

        for (Language language : values()) {
            if (language.getName().equalsIgnoreCase(value)) {
                return language;
            }
        }

        throw new UnsupportedTypeException("Language = [" + value + "] is not supported by app!");
    }
}
