package ru.akvine.iskra.services.dto.dictionary;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class ListDictionaries {
    private Set<String> names;
    private boolean system;
}
