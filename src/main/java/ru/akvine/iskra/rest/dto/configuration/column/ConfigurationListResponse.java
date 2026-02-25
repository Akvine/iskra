package ru.akvine.iskra.rest.dto.configuration.column;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;

@Data
@Accessors(chain = true)
public class ConfigurationListResponse extends SuccessfulResponse {
    private int count;
    private List<ConfigurationDto> configurations;
}
