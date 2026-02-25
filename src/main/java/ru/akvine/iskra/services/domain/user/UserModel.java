package ru.akvine.iskra.services.domain.user;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.Language;
import ru.akvine.iskra.repositories.entities.UserEntity;
import ru.akvine.iskra.services.domain.base.SoftModel;

@Data
@Accessors(chain = true)
public class UserModel extends SoftModel<Long> {
    private String uuid;

    @ToString.Exclude
    private String email;

    private String username;

    @ToString.Exclude
    private String password;

    private Language language;

    public UserModel(UserEntity user) {
        super(user);
        this.uuid = user.getUuid();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.language = user.getLanguage();
    }
}
