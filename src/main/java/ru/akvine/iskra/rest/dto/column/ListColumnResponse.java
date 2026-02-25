package ru.akvine.iskra.rest.dto.column;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;
import ru.akvine.iskra.rest.dto.table.ColumnDto;

@Data
@Accessors(chain = true)
public class ListColumnResponse extends SuccessfulResponse {
    private List<ColumnDto> columns;
}
