package ru.akvine.iskra.utils;

import jakarta.annotation.Nullable;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@UtilityClass
public class StringHelper {

    public String replaceAroundMiddle(List<String> words, int visibleCount) {
        if (CollectionUtils.isEmpty(words)) {
            return "[]";
        }

        int size = words.size();

        if (size <= visibleCount * 2) {
            return words.toString();
        }

        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < visibleCount; i++) {
            result.append(words.get(i)).append(", ");
        }
        result.append("..., ");
        for (int i = size - visibleCount; i < size; i++) {
            result.append(words.get(i));
            if (i < size - 1) result.append(", ");
        }
        result.append("]");

        return result.toString();
    }

    @Nullable
    public static String trim(String value, Integer maxLength) {
        return trim(value, maxLength, false);
    }

    @Nullable
    public static String trim(String value, Integer maxLength, boolean before) {
        if (value == null) {
            return null;
        }
        if (before) {
            return value.length() > maxLength ? value.substring(0, maxLength) : value;
        }

        return value.length() > maxLength ? value.substring(value.length() - maxLength) : value;
    }
}
