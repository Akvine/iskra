package ru.akvine.iskra.utils;

import jakarta.annotation.Nullable;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class StringHelper {

    public String mask(String word) {
        return mask(word, "*");
    }

    public String mask(String word, String replacement) {
        if (StringUtils.isBlank(word)) {
            return "";
        }
        int codeUnitLength = word.length();

        int codePointCount = word.codePointCount(0, codeUnitLength);
        int maskLengthInCodePoints = (int) Math.ceil(0.75 * codePointCount);
        int visibleLengthInCodePoints = codePointCount - maskLengthInCodePoints;

        if (visibleLengthInCodePoints == 0) {
            return "*".repeat(codeUnitLength);
        }

        int leftVisibleInCodePoints = visibleLengthInCodePoints / 2;
        int rightVisibleInCodePoints = visibleLengthInCodePoints - leftVisibleInCodePoints;

        int leftEndIndex = word.offsetByCodePoints(0, leftVisibleInCodePoints);
        int rightStartIndex = word.offsetByCodePoints(0, codePointCount - rightVisibleInCodePoints);

        String leftPart = word.substring(0, leftEndIndex);
        String rightPart = word.substring(rightStartIndex, codeUnitLength);
        int maskedPartLength = rightStartIndex - leftEndIndex;
        String maskedPart = replacement.repeat(maskedPartLength);

        return leftPart + maskedPart + rightPart;
    }

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
