package ru.akvine.iskra.rest.dto.process;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;

import java.util.List;

@Data
@Accessors(chain = true)
public class TableProcessListResponse extends SuccessfulResponse {
    private List<TableProcessDto> tableProcesses;
}
