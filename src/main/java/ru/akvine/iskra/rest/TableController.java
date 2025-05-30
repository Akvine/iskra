package ru.akvine.iskra.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.converter.TableConverter;
import ru.akvine.iskra.rest.dto.table.ToggleSelectedRequest;
import ru.akvine.iskra.rest.meta.TableControllerMeta;
import ru.akvine.iskra.services.domain.table.TableService;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.dto.table.ToogleSelectedTables;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TableController implements TableControllerMeta {
    private final TableConverter tableConverter;
    private final TableService tableService;

    @Override
    public Response toggleSelected(@RequestBody @Valid ToggleSelectedRequest request) {
        ToogleSelectedTables action = tableConverter.convertToToggleSelectedTables(request);
        List<TableModel> toggled = tableService.toggleSelected(action);
        return tableConverter.convertToListTablesResponse(toggled);
    }
}
