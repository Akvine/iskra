package ru.akvine.iskra.services.dto.user;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.Language;

@Data
@Accessors(chain = true)
public class CreateUser {
    private String username;
    private String email;
    private String password;
    private Language language;
}
