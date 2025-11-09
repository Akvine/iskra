package ru.akvine.iskra.rest.dto.table;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ReferenceInfoDto {
    private String targetColumnNameForForeignKey;
    private String targetTableNameForForeignKey;
    private String relationsShipType;
}
