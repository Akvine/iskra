package ru.akvine.iskra.rest.dto.dictionary;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.dto.NextPage;

import java.util.HashSet;
import java.util.Set;

@Data
@Accessors(chain = true)
public class ListDictionariesRequest {
    private Set<String> names = new HashSet<>();
    private boolean system;
    @NotNull
    private NextPage nextPage;
}
