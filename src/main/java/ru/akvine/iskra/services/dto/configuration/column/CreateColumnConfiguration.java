package ru.akvine.iskra.services.dto.configuration.column;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.istochnik.ConverterDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)
public class CreateColumnConfiguration {
    private String userUuid;
    private String columnUuid;
    private String name;
    private Set<String> dictionariesUuids = new HashSet<>();
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
    private List<ConverterDto> converters = new ArrayList<>();
    private List<ConverterDto> postConverters = new ArrayList<>();
}
