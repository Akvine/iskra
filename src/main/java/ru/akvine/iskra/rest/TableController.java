package ru.akvine.iskra.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.converter.TableConverter;
import ru.akvine.iskra.rest.meta.TableControllerMeta;
import ru.akvine.iskra.services.TableService;
import ru.akvine.iskra.services.dto.table.TableModel;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TableController implements TableControllerMeta {
    private final TableService tableService;
    private final TableConverter tableConverter;

    @Override
    public Response createOrListIfExist(@RequestParam("planUuid") String planUuid) {
        List<TableModel> tables = tableService.createOrListIfExist(planUuid);
        return tableConverter.convertToListTablesResponse(tables);
    }
}
