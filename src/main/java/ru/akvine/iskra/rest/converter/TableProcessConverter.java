package ru.akvine.iskra.rest.converter;

import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.rest.dto.process.TableProcessDto;
import ru.akvine.iskra.rest.dto.process.TableProcessListResponse;
import ru.akvine.iskra.services.domain.TableProcessModel;

import java.util.List;

@Component
public class TableProcessConverter {
    public TableProcessListResponse convertToTableProcessListResponse(List<TableProcessModel> tableProcesses) {
        Asserts.isNotNull(tableProcesses);
        return new TableProcessListResponse()
                .setTableProcesses(tableProcesses.stream().map(this::buildTableProcessDto).toList());
    }

    private TableProcessDto buildTableProcessDto(TableProcessModel tableProcess) {
        return new TableProcessDto()
                .setPid(tableProcess.getPid())
                .setTableName(tableProcess.getTableName())
                .setProcessState(tableProcess.getProcessState().toString())
                .setErrorMessage(tableProcess.getErrorMessage())
                .setCompletedDate(tableProcess.getCompletedDate());
    }
}
