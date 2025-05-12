package ru.akvine.iskra.rest.converter;

import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.rest.dto.table.ListTablesResponse;
import ru.akvine.iskra.rest.dto.table.TableDto;
import ru.akvine.iskra.services.dto.table.TableModel;

import java.util.List;

@Component
public class TableConverter {
    public ListTablesResponse convertToListTablesResponse(List<TableModel> tables) {
        Asserts.isNotNull(tables);
        return new ListTablesResponse().setTables(tables.stream().map(this::buildTableDto).toList());
    }

    private TableDto buildTableDto(TableModel table) {
        return new TableDto().setTableName(table.getTableName());
    }
}
