package ru.akvine.iskra.services.domain.table.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class ToogleSelectedTables {
    private String userUuid;
    private String planUuid;
    private Map<String, Boolean> toggled;
}
