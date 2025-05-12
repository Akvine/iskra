package ru.akvine.iskra.services.integration.visor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ConnectionRequest {
    private String databaseName;

    private String schema;

    @NotBlank
    private String databaseType;

    @NotBlank
    private String host;

    @NotBlank
    private String port;

    @ToString.Exclude
    private String username;

    @ToString.Exclude
    private String password;
}
