package ru.akvine.iskra.rest.mappers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.AuthResponse;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.enums.Language;
import ru.akvine.iskra.rest.dto.security.RegistrationRequest;
import ru.akvine.iskra.services.domain.user.UserModel;
import ru.akvine.iskra.services.domain.user.dto.CreateUser;

@Component
public class UserMapper {
    public CreateUser mapToCreateUser(RegistrationRequest request) {
        Asserts.isNotNull(request);
        return new CreateUser()
                .setEmail(request.getEmail())
                .setUsername(request.getUsername())
                .setPassword(request.getPassword())
                .setLanguage(
                        StringUtils.isBlank(request.getLanguage())
                                ? Language.RU
                                : Language.from(request.getLanguage()));
    }

    public AuthResponse mapToAuthResponse(UserModel user) {
        Asserts.isNotNull(user);
        return new AuthResponse()
                .setUuid(user.getUuid())
                .setEmail(user.getEmail())
                .setUsername(user.getUsername());
    }
}
