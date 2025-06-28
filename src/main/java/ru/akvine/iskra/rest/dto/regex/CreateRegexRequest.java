package ru.akvine.iskra.rest.dto.regex;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateRegexRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String pattern;

    private String description;
}
