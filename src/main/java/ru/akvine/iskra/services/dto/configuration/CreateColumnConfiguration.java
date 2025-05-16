package ru.akvine.iskra.services.dto.configuration;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class CreateColumnConfiguration {
    private String columnUuid;
    private String name;
    @Nullable
    private String dictionaryName;
    private boolean selected;
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
}
