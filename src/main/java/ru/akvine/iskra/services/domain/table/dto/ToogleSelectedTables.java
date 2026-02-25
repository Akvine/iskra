package ru.akvine.iskra.services.domain.table.dto;

import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ToogleSelectedTables {
    private String userUuid;
    private String planUuid;
    private Map<String, Boolean> toggled;
}
