package ru.akvine.iskra.rest.dto.table;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateTablesRequest {
    @NotBlank
    private String planUuid;
}
