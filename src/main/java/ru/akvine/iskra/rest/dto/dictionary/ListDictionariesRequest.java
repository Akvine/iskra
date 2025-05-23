package ru.akvine.iskra.rest.dto.dictionary;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Data
@Accessors(chain = true)
public class ListDictionariesRequest {
    private Set<String> names = new HashSet<>()
            ;
    private boolean system;
}
