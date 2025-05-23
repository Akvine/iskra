package ru.akvine.iskra.services.dto.configuration.column;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SelectColumnConfiguration {
    private String name;
    private String columnUuid;
}
