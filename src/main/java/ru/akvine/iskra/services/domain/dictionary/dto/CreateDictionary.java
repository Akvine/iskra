package ru.akvine.iskra.services.domain.dictionary.dto;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class CreateDictionary {
    private String userUuid;
    private String name;
    private Set<String> values;
    @Nullable
    private String description;
    private String language;
}
