package ru.akvine.iskra.services.domain.column.configuration;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import ru.akvine.compozit.commons.istochnik.FilterDto;
import ru.akvine.iskra.repositories.entities.config.ColumnConfigurationEntity;
import ru.akvine.iskra.services.domain.base.Model;
import ru.akvine.iskra.services.domain.dictionary.DictionaryModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class ColumnConfigurationModel extends Model<Long> {
    private boolean selected;
    private String name;
    private String type;
    private String generationStrategy;
    private boolean unique;
    private boolean notNull;
    private boolean convertToString;
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
    @Nullable
    private DictionaryModel dictionary;
    private Long columnId;
    private boolean repeatable;
    private List<FilterDto> filters = List.of();
    private List<FilterDto> postFilters = List.of();

    public ColumnConfigurationModel(ColumnConfigurationEntity entity) {
        super(entity);

        this.selected = entity.isSelected();
        this.convertToString = entity.isConvertToString();
        this.name = entity.getName();
        this.type = entity.getType();
        this.generationStrategy = entity.getGenerationStrategy();
        this.unique = entity.isUnique();
        this.notNull = entity.isNotNull();
        this.rangeType = entity.getRangeType();
        this.start = entity.getStart();
        this.step = entity.getStep();
        this.end = entity.getEnd();
        this.valid = entity.getValid();
        this.columnId = entity.getColumn().getId();
        this.repeatable = entity.isRepeatable();

        if (StringUtils.isNotBlank(entity.getRegexps())) {
            this.regexps = Arrays.stream(entity.getRegexps().split(";")).toList();
        }
        if (entity.getDictionary() != null) {
            this.dictionary = new DictionaryModel(entity.getDictionary());
        }
        if (CollectionUtils.isNotEmpty(entity.getFilters())) {
            this.filters = entity.getFilters();
        }
        if (CollectionUtils.isNotEmpty(entity.getPostFilters())) {
            this.postFilters = entity.getPostFilters();
        }
    }
}
