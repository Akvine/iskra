package ru.akvine.iskra.rest.dto.column;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SelectColumnRequest {
    @NotBlank
    private String planUuid;

    @NotBlank
    private String tableName;

    @NotNull
    private Map<String, Boolean> selected;
}
