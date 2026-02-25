package ru.akvine.iskra.enums;

import io.micrometer.common.util.StringUtils;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// TODO: дублирование enum из DBVisor
public enum ConstraintType {
    TRIGGER("trigger", List.of()),
    FOREIGN_KEY("foreign_key", List.of("f", "F", "R", "FOREIGN KEY")),
    UNIQUE("unique", List.of("u", "U", "UNIQUE")),
    IDENTITY("identity", List.of("IDENTITY")),
    INDEX("index", List.of("i")),
    PRIMARY_KEY("primary_key", List.of("p", "P", "PRIMARY KEY")),
    CHECK("check", List.of("c", "C", "CHECK")),
    DEFAULT("default", List.of("d", "D")),
    NOT_NULL("not_null", List.of("n", "N"));

    private final String name;
    private final List<String> codes;

    public static ConstraintType fromName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Constraint type name can't be blank!");
        }

        for (ConstraintType constraintType : values()) {
            if (constraintType.getName().equalsIgnoreCase(name)) {
                return constraintType;
            }
        }

        throw new UnsupportedOperationException("Constraint type with name = [" + name + "] is not supported by app!");
    }

    public static ConstraintType fromCode(String code) {
        if (StringUtils.isBlank(code)) {
            throw new IllegalArgumentException("Constraint type code can't be blank!");
        }

        for (ConstraintType type : values()) {
            List<String> codes = type.getCodes();

            for (String constraintCode : codes) {
                if (constraintCode.equals(code)) {
                    return type;
                }
            }
        }

        throw new UnsupportedOperationException("Constraint type with code = [" + code + "] is not supported by app!");
    }
}
