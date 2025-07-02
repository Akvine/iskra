package ru.akvine.iskra.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.dto.table.ListTablesRequest;
import ru.akvine.iskra.rest.mappers.TableMapper;
import ru.akvine.iskra.rest.dto.table.ToggleSelectedRequest;
import ru.akvine.iskra.rest.meta.TableControllerMeta;
import ru.akvine.iskra.services.domain.table.TableService;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.domain.table.dto.ListTables;
import ru.akvine.iskra.services.domain.table.dto.ToogleSelectedTables;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TableController implements TableControllerMeta {
    private final TableMapper tableMapper;
    private final TableService tableService;

    @Override
    public Response list(ListTablesRequest request) {
        ListTables listTables = tableMapper.mapToListTables(request);
        List<TableModel> tables = tableService.list(listTables);
        return tableMapper.mapToListTablesResponse(tables);
    }

    @Override
    public Response toggleSelected(@RequestBody @Valid ToggleSelectedRequest request) {
        ToogleSelectedTables action = tableMapper.mapToToggleSelectedTables(request);
        List<TableModel> toggled = tableService.toggleSelected(action);
        return tableMapper.mapToListTablesResponse(toggled);
    }
}
