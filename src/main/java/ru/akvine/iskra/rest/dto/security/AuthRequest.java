package ru.akvine.iskra.rest.dto.security;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AuthRequest {
    @ToString.Exclude
    @NotBlank
    protected String username;

    @ToString.Exclude
    @NotBlank
    protected String password;
}
