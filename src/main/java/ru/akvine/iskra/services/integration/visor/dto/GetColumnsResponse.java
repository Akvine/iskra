package ru.akvine.iskra.services.integration.visor.dto;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetColumnsResponse {
    private List<ColumnMetadataDto> columns;
}
