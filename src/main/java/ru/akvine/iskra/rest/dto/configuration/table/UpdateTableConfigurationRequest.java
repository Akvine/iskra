package ru.akvine.iskra.rest.dto.configuration.table;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateTableConfigurationRequest {
    @NotBlank
    private String planUuid;

    @NotBlank
    private String tableName;

    private String name;

    private Integer rowsCount;

    private Integer batchSize;

    private Boolean generateClearScript;

    private Boolean deleteDataBeforeStart;

    private String deleteMode;
}
