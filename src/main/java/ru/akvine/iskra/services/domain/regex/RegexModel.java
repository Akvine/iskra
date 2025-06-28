package ru.akvine.iskra.services.domain.regex;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.repositories.entities.RegexEntity;
import ru.akvine.iskra.services.domain.base.SoftModel;
import ru.akvine.iskra.services.domain.user.UserModel;

@Data
@Accessors(chain = true)
public class RegexModel extends SoftModel<Long> {
    private String uuid;
    private String pattern;
    private boolean system;
    private String name;
    @Nullable
    private String description;
    private UserModel user;

    public RegexModel(RegexEntity regexEntity) {
        super(regexEntity);
        this.uuid = regexEntity.getUuid();
        this.name = regexEntity.getName();
        this.pattern = regexEntity.getPattern();
        this.description = regexEntity.getDescription();

        if (regexEntity.getUser() != null) {
            this.user = new UserModel(regexEntity.getUser());
        }
    }
}
