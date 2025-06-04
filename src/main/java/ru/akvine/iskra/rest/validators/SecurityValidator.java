package ru.akvine.iskra.rest.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.rest.dto.security.RegistrationRequest;
import ru.akvine.iskra.validators.EmailValidator;
import ru.akvine.iskra.validators.PasswordValidator;

@Component
@RequiredArgsConstructor
public class SecurityValidator {
    private final EmailValidator emailValidator;
    private final PasswordValidator passwordValidator;

    public void verifyRegistrationRequest(RegistrationRequest request) {
        Asserts.isNotNull(request);
        emailValidator.validate(request.getEmail());
        passwordValidator.validate(request.getPassword());
    }
}
