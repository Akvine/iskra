package ru.akvine.iskra.rest.dto.configuration.column;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Data
@Accessors(chain = true)
public class CreateConfigurationRequest {
    @NotBlank
    private String columnUuid;

    @NotBlank
    private String name;

    private String dictionaryName;

    private boolean selected;

    @NotBlank
    private String type;

    @NotBlank
    private String generationStrategy;

    private boolean unique;

    private boolean notNull;

    @NotBlank
    private String rangeType;

    private String start;

    private String end;

    private String step;

    private Boolean valid;

    private Set<String> regexps = new HashSet<>();
}
