package ru.akvine.iskra.services.domain.column.configuration.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SelectColumnConfiguration {
    private String name;
    private String columnUuid;
}
