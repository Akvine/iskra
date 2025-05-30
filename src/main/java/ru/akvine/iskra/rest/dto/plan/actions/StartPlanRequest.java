package ru.akvine.iskra.rest.dto.plan.actions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.Set;

@Data
@Accessors(chain = true)
public class StartPlanRequest {
    @NotBlank
    private String planUuid;

    @NotNull
    private Collection<String> tableNames = Set.of();
}
