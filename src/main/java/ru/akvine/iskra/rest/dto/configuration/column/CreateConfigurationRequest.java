package ru.akvine.iskra.rest.dto.configuration.column;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.istochnik.FilterDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)
public class CreateConfigurationRequest {
    @NotBlank
    private String columnUuid;

    @NotBlank
    private String name;

    private String dictionaryUuid;

    private boolean selected;

    // TODO: добавить валидацю для данного поля (Его можно проставить в false только для Numeric-полей)
    private boolean repeatable = true;

    @NotBlank
    private String type;

    @NotBlank
    private String generationStrategy;

    private boolean unique;

    private boolean notNull;

    @NotBlank
    private String rangeType;

    private String start;

    private String end;

    private String step;

    private Boolean valid;

    private boolean convertToString;

    // TODO: нужно вместо Set сделать List, т.к. пользователь может захотеть генерировать данные по списку выражений
    private Set<String> regexps = new HashSet<>();

    private List<FilterDto> filters = new ArrayList<>();

    private List<FilterDto> postFilters = new ArrayList<>();
}
