package ru.akvine.iskra.rest.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.repositories.entities.UserEntity;
import ru.akvine.iskra.services.UserService;
import ru.akvine.iskra.services.domain.user.UserModel;

@Component
@RequiredArgsConstructor
public class CredentialsValidator {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public UserModel validateCredentialsByEmail(String email, String password) {
        Asserts.isNotBlank(email, "email is null");
        Asserts.isNotBlank(password, "password is null");

        UserEntity userEntity = userService.verifyExistsByEmail(email);
        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new BadCredentialsException("Bad credentials!");
        }

        return new UserModel(userEntity);
    }

    public UserModel validateCredentialsByUsername(String username, String password) {
        Asserts.isNotBlank(username, "username is null");
        Asserts.isNotBlank(password, "password is null");

        UserEntity userEntity = userService.verifyExistsByUsername(username);
        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new BadCredentialsException("Bad credentials!");
        }

        return new UserModel(userEntity);
    }
}
