package ru.akvine.iskra.services.domain.column.configuration.dictionary;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.repositories.entities.config.ColumnConfigurationDictionaryEntity;
import ru.akvine.iskra.services.domain.base.SoftModel;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class ColumnConfigurationDictionaryModel extends SoftModel<Long> {
    private Long columnConfigurationId;
    private String dictionaryName;
    private Set<String> dictionaryValues;

    public ColumnConfigurationDictionaryModel(ColumnConfigurationDictionaryEntity entity) {
        super(entity);
        this.columnConfigurationId = entity.getColumnConfiguration().getId();
        this.dictionaryName = entity.getDictionary().getName();
        this.dictionaryValues = Arrays.stream(entity.getDictionary().getValues().split(","))
                .collect(Collectors.toSet());
    }
}
