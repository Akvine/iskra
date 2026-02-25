package ru.akvine.iskra.services.domain.table.process.dto;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ListTableProcess {
    private String planUuid;

    @Nullable
    private String processUuid;
}
