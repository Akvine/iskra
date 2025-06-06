package ru.akvine.iskra.services.integration.visor.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class GetTableResponse {
    private List<TableMetadataDto> tables;
}
