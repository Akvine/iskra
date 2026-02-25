package ru.akvine.iskra.repositories.dto;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DictionariesFilter {
    private String userUuid;
    private Set<String> names = new HashSet<>();
}
