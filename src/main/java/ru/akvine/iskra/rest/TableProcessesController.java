package ru.akvine.iskra.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.converter.TableProcessConverter;
import ru.akvine.iskra.rest.meta.TableProcessControllerMeta;
import ru.akvine.iskra.services.TableProcessService;
import ru.akvine.iskra.services.domain.TableProcessModel;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TableProcessesController implements TableProcessControllerMeta {
    private final TableProcessService tableProcessService;
    private final TableProcessConverter tableProcessConverter;

    @Override
    public Response list(@PathVariable("uuid") String planUuid) {
        List<TableProcessModel> tableProcesses = tableProcessService.list(planUuid);
        return tableProcessConverter.convertToTableProcessListResponse(tableProcesses);
    }
}
