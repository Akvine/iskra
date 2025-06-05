package ru.akvine.iskra.services.domain.dictionary;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.Language;
import ru.akvine.iskra.repositories.entities.DictionaryEntity;
import ru.akvine.iskra.services.domain.base.Model;
import ru.akvine.iskra.services.domain.user.UserModel;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class DictionaryModel extends Model<Long> {
    private String uuid;
    private String name;
    private boolean system;
    @Nullable
    private String description;
    private Language language;
    private Set<String> values;
    private UserModel user;

    public DictionaryModel(DictionaryEntity entity) {
        super(entity);

        this.uuid = entity.getUuid();
        this.name = entity.getName();
        this.language = entity.getLanguage();
        this.system = entity.isSystem();
        this.description = entity.getDescription();
        this.values = Arrays.stream(entity.getValues().split(","))
                .collect(Collectors.toSet());
        if (!entity.isSystem()) {
            this.user = new UserModel(entity.getUser());
        }
    }
}
