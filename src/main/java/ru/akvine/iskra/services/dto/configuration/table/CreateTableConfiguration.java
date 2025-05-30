package ru.akvine.iskra.services.dto.configuration.table;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.enums.DeleteMode;

@Data
@Accessors(chain = true)
public class CreateTableConfiguration {
    private String planUuid;
    private String tableName;
    private String name;
    private int rowsCount;
    private int batchSize;
    private boolean deleteDataBeforeStart;
    @Nullable
    private Boolean generateClearScript;
    @Nullable
    private DeleteMode deleteMode;
}
