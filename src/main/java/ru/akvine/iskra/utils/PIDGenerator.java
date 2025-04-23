package ru.akvine.iskra.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;

@UtilityClass
public class PIDGenerator {
    public String generate(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("PID length can't be less than 1");
        }

        return RandomStringUtils.randomNumeric(length);
    }
}
