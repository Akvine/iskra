package ru.akvine.iskra.rest.dto.table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class ToggleSelectedRequest {
    @NotBlank
    private String planUuid;

    @NotNull
    private Map<String, Boolean> toggled = new HashMap<>();
}
