package ru.akvine.iskra.services.domain.table.configuration.dto;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.enums.DeleteMode;

@Data
@Accessors(chain = true)
public class CreateTableConfiguration {
    private String userUuid;
    private String planUuid;
    private String tableName;
    private String name;
    private int rowsCount;
    private int batchSize;

    @Nullable
    private Boolean generateClearScript;

    @Nullable
    private DeleteMode deleteMode;
}
