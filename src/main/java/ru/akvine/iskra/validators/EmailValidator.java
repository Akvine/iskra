package ru.akvine.iskra.validators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.iskra.constants.ApiErrorCodes;
import ru.akvine.iskra.exceptions.ValidationException;

import java.util.regex.Pattern;

@Component
public class EmailValidator implements Validator<String> {
    private static final String EMAIL_PATTERN =
            "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public void validate(String email) {
        if (StringUtils.isBlank(email)) {
            throw new ValidationException(
                    ApiErrorCodes.Validation.EMAIL_INVALID_ERROR,
                    "Email is blank");
        }
        if (!Pattern.matches(EMAIL_PATTERN, email)) {
            throw new ValidationException(
                    ApiErrorCodes.Validation.EMAIL_INVALID_ERROR,
                    "Email is invalid"
            );
        }
    }
}
