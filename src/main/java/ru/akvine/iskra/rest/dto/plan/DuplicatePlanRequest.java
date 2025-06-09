package ru.akvine.iskra.rest.dto.plan;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DuplicatePlanRequest {
    @NotBlank
    private String uuid;

    private boolean copyResults;
}
