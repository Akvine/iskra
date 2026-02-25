package ru.akvine.iskra.rest.dto.table;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;

@Data
@Accessors(chain = true)
public class ListTablesResponse extends SuccessfulResponse {
    private List<TableDto> tables;
}
