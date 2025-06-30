package ru.akvine.iskra.services.dto.configuration.table;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.enums.DeleteMode;

import java.util.List;

@Data
@Accessors(chain = true)
public class UpdateTableConfiguration {
    private String userUuid;
    private String planUuid;
    private String tableName;
    @Nullable
    private String name;
    @Nullable
    private Integer rowsCount;
    @Nullable
    private Integer batchSize;
    @Nullable
    private Boolean generateClearScript;
    @Nullable
    private Boolean deleteDataBeforeStart;
    @Nullable
    private DeleteMode deleteMode;
    private List<String> createScripts = List.of();
    private List<String> dropScripts = List.of();
}
