package ru.akvine.iskra.services.dto.dictionary;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class CreateDictionary {
    private String name;
    private Set<String> values;
    @Nullable
    private String description;
    private String language;
}
