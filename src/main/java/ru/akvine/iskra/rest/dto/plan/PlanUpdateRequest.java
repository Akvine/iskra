package ru.akvine.iskra.rest.dto.plan;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PlanUpdateRequest {
    @NotBlank
    private String planUuid;

    private String name;

    private Boolean generateScriptsForNotNull;

    private Boolean generateScriptsForIndex;

    private Boolean generateScriptsForPrimaryKey;

    private Boolean generateScriptsForTrigger;

    private Boolean generateScriptsForUnique;

    private Boolean generateScriptsForCheck;

    private Boolean generateScriptsForDefault;
}
