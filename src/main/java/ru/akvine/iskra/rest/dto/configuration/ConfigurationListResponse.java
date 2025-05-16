package ru.akvine.iskra.rest.dto.configuration;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;

import java.util.List;

@Data
@Accessors(chain = true)
public class ConfigurationListResponse extends SuccessfulResponse {
    private int count;
    private List<ConfigurationDto> configurations;
}
