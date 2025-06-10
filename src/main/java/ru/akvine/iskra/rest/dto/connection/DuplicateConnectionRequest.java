package ru.akvine.iskra.rest.dto.connection;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DuplicateConnectionRequest {
    @NotBlank
    private String connectionName;

    private String newConnectionName;
}
