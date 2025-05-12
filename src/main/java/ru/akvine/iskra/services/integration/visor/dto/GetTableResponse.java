package ru.akvine.iskra.services.integration.visor.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.services.dto.TableMetadataDto;

import java.util.List;

@Data
@Accessors(chain = true)
public class GetTableResponse {
    private List<TableMetadataDto> tables;
}
