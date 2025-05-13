package ru.akvine.iskra.rest.dto.dictionary;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class CreateDictionaryRequest {
    @NotBlank
    private String name;

    @NotNull
    private Set<String> values;

    private String description;
}
