package ru.akvine.iskra.services.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Row {
    private String tableName;
    private boolean[] values;

    public boolean hasRelationsShip() {
        for (boolean value : values) {
            if (value) {
                return true;
            }
        }

        return false;
    }
}
