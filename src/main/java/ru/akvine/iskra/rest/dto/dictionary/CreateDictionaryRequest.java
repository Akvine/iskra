package ru.akvine.iskra.rest.dto.dictionary;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateDictionaryRequest {
    @NotBlank
    private String name;

    private String language;

    @NotNull
    private Set<String> values;

    private String description;
}
