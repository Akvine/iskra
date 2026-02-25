package ru.akvine.iskra.services.domain.column.dto;

import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SelectColumn {
    private String planUuid;
    private String tableName;
    private Map<String, Boolean> selected;
}
