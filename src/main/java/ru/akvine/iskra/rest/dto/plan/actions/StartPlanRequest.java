package ru.akvine.iskra.rest.dto.plan.actions;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StartPlanRequest {
    @NotBlank
    private String planUuid;

    private boolean resume;
}
