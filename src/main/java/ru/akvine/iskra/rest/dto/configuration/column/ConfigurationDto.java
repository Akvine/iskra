package ru.akvine.iskra.rest.dto.configuration.column;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.istochnik.FilterDto;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class ConfigurationDto {
    private String name;
    @Nullable
    private String dictionaryName;
    private boolean selected;
    private boolean repeatable;
    private boolean convertToString;
    private String type;
    private String generationStrategy;
    private boolean unique;
    private boolean notNull;
    private String rangeType;
    private String start;
    private String end;
    private String step;
    private Boolean valid;
    private List<String> regexps = new ArrayList<>();
    private List<FilterDto> filters = List.of();
    private List<FilterDto> postFilters = List.of();
}
