package ru.akvine.iskra.rest.dto.connection;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.ConnectionDto;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;

import java.util.List;

@Data
@Accessors(chain = true)
public class ConnectionResponse extends SuccessfulResponse {
    private List<ConnectionDto> connections;
}
