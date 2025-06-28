package ru.akvine.iskra.services.dto.regex;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateRegex {
    private String userUuid;
    private String name;
    private String pattern;
    @Nullable
    private String description;
}
