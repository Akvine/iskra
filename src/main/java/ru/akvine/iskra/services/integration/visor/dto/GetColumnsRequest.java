package ru.akvine.iskra.services.integration.visor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.ConnectionRequest;

@Data
@Accessors(chain = true)
public class GetColumnsRequest {
    @NotNull
    private ConnectionRequest connection;

    @NotBlank
    private String tableName;
}
