package ru.akvine.iskra.services.dto.configuration.column;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.istochnik.FilterDto;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class CreateColumnConfiguration {
    private String userUuid;
    private String columnUuid;
    private String name;
    @Nullable
    private String dictionaryUuid;
    private boolean selected;
    private boolean convertToString;
    private boolean repeatable;
    private String type;
    private String generationStrategy;
    private boolean unique;
    private boolean notNull;
    private String rangeType;
    @Nullable
    private String start;
    @Nullable
    private String end;
    @Nullable
    private String step;
    @Nullable
    private Boolean valid;
    private List<String> regexps = new ArrayList<>();
    private List<FilterDto> filters = new ArrayList<>();
    private List<FilterDto> postFilters = new ArrayList<>();
}
