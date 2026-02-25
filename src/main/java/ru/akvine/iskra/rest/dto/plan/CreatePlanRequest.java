package ru.akvine.iskra.rest.dto.plan;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreatePlanRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String connectionName;
}
