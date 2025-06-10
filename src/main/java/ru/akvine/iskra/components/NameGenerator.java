package ru.akvine.iskra.components;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class NameGenerator {
    public List<String> tryGetIncrementedNames(String name, int count) {
        Objects.requireNonNull(name, "No name specified");
        if (count < 1) {
            throw new IllegalArgumentException("The number must be greater than zero");
        }
        if (!Character.isDigit(name.charAt(name.length()-1))) {
            return List.of();
        }

        StringBuilder digitsSb = new StringBuilder();
        int zeroesCount = 0;
        int sliceIndex;

        for (sliceIndex = name.length()-1; sliceIndex >= 0; sliceIndex--) {
            char c = name.charAt(sliceIndex);
            if (!Character.isDigit(c)) {
                break;
            }
            digitsSb.insert(0, c);
            if (c == '0') {
                if (digitsSb.length() > 1) {
                    zeroesCount++;
                }
            } else {
                zeroesCount = 0;
            }
        }

        long number = Long.parseLong(digitsSb.toString());
        int digitsCount = String.valueOf(number).length();
        List<String> result = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            if (number - 1L + i == Long.MAX_VALUE) {
                return result;
            }
            String incNumberStr = String.valueOf(number + i);
            zeroesCount -= incNumberStr.length() - digitsCount;

            String incName = name.substring(0, Math.max(0, sliceIndex + 1))
                    + "0".repeat(Math.max(0, zeroesCount))
                    + incNumberStr;

            result.add(incName);
        }

        return result;
    }
}
