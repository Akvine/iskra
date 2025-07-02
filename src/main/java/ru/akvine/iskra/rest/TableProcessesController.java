package ru.akvine.iskra.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.mappers.TableProcessMapper;
import ru.akvine.iskra.rest.meta.TableProcessControllerMeta;
import ru.akvine.iskra.services.domain.table.process.TableProcessService;
import ru.akvine.iskra.services.domain.table.process.TableProcessModel;
import ru.akvine.iskra.services.domain.table.process.dto.ListTableProcess;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TableProcessesController implements TableProcessControllerMeta {
    private final TableProcessService tableProcessService;
    private final TableProcessMapper tableProcessMapper;

    @Override
    public Response list(@PathVariable("uuid") String planUuid,
                         @PathVariable(value = "processUuid", required = false) String processUuid) {
        ListTableProcess action = tableProcessMapper.mapToListTableProcess(planUuid, processUuid);
        List<TableProcessModel> tableProcesses = tableProcessService.list(action);
        return tableProcessMapper.mapToTableProcessListResponse(tableProcesses);
    }
}
