package ru.akvine.iskra.enums;

import org.apache.commons.lang3.StringUtils;

public enum CacheType {
    INSTANCE;

    public static CacheType from(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("Cache type can't be null");
        }

        for (CacheType type : values()) {
            if (type.toString().toLowerCase().equalsIgnoreCase(value)) {
                return type;
            }
        }

        throw new UnsupportedOperationException("Cache type = [" + value + "] is not supported by app!");
    }
}
