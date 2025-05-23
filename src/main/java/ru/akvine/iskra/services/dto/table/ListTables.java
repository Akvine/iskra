package ru.akvine.iskra.services.dto.table;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ListTables {
    private String planUuid;
    private boolean selected;
}
