package ru.akvine.iskra.services.domain.column.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class SelectColumn {
    private String planUuid;
    private String tableName;
    private Map<String, Boolean> selected;
}
