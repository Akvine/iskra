package ru.akvine.iskra.services.domain.dictionary.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.PageInfo;

import java.util.Set;

@Data
@Accessors(chain = true)
public class ListDictionaries {
    private String userUuid;
    private Set<String> names;
    private boolean system;
    private PageInfo pageInfo;
}
