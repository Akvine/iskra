package ru.akvine.iskra.services.domain;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import ru.akvine.iskra.repositories.entities.ColumnConfigurationEntity;
import ru.akvine.iskra.services.domain.base.Model;

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
    private ColumnModel column;

    public ColumnConfigurationModel(ColumnConfigurationEntity entity) {
        super(entity);

        this.selected = entity.isSelected();
        this.name = entity.getName();
        this.type = entity.getType();
        this.generationStrategy = entity.getGenerationStrategy();
        this.unique = entity.isUnique();
        this.notNull = entity.isNotNull();
        this.rangeType = entity.getRangeType();
        this.start = entity.getStart();
        this.end = entity.getEnd();
        this.valid = entity.getValid();
        this.column = new ColumnModel(entity.getColumn());

        if (StringUtils.isNotBlank(entity.getRegexps())) {
            this.regexps = Arrays.stream(entity.getRegexps().split(";")).toList();
        }
        if (entity.getDictionary() != null) {
            this.dictionary = new DictionaryModel(entity.getDictionary());
        }
    }
}
