package ru.akvine.iskra.rest.dto.configuration.table;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;

import java.util.List;

@Data
@Accessors(chain = true)
public class ListTableConfigurationsResponse extends SuccessfulResponse {
    private List<TableConfigurationDto> configurations;
}
