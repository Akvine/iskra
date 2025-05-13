package ru.akvine.iskra.utils;

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
}
