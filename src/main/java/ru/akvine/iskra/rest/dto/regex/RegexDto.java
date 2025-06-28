package ru.akvine.iskra.rest.dto.regex;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RegexDto {
    private String name;
    private String pattern;
    private String description;
    private boolean system;
}
