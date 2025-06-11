package ru.akvine.iskra.repositories.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Data
@Accessors(chain = true)
public class DictionariesFilter {
    private String userUuid;
    private Set<String> names = new HashSet<>();
}
