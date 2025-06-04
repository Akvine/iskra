package ru.akvine.iskra.rest.dto.security;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RegistrationRequest extends AuthRequest {
    @NotBlank
    private String email;

    private String language;
}
