package ru.akvine.iskra.services.domain.table.dto;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ListTables {
    private String userUuid;
    private String planUuid;

    @Nullable
    private Boolean selected;

    private boolean resume;
}
