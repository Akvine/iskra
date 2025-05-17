package ru.akvine.iskra.rest.dto.configuration;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SelectConfigurationRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String columnUuid;
}
