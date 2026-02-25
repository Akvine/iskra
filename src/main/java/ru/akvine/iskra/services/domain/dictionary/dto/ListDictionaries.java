package ru.akvine.iskra.services.domain.dictionary.dto;

import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.PageInfo;

@Data
@Accessors(chain = true)
public class ListDictionaries {
    private String userUuid;
    private Set<String> names;
    private boolean system;
    private PageInfo pageInfo;
}
