package ru.akvine.iskra.rest.dto.configuration.table;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateTableConfigurationRequest {
    @NotBlank
    private String planUuid;

    @NotBlank
    private String tableName;

    @NotBlank
    private String name;

    @Min(1)
    private int rowsCount;

    @Min(1)
    private int batchSize;

    private Boolean generateClearScript;

    private Boolean deleteDataBeforeStart;

    private String deleteMode;
}
