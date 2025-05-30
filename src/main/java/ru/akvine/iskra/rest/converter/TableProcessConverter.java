package ru.akvine.iskra.rest.converter;

import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.rest.dto.process.TableProcessDto;
import ru.akvine.iskra.rest.dto.process.TableProcessListResponse;
import ru.akvine.iskra.services.domain.table.process.TableProcessModel;
import ru.akvine.iskra.services.dto.process.ListTableProcess;

import java.util.List;

@Component
public class TableProcessConverter {

    public ListTableProcess convertToListTableProcess(String planUuid, String processUuid) {
        return new ListTableProcess()
                .setPlanUuid(planUuid)
                .setProcessUuid(processUuid);
    }

    public TableProcessListResponse convertToTableProcessListResponse(List<TableProcessModel> tableProcesses) {
        Asserts.isNotNull(tableProcesses);
        return new TableProcessListResponse()
                .setTableProcesses(tableProcesses.stream().map(this::buildTableProcessDto).toList());
    }

    private TableProcessDto buildTableProcessDto(TableProcessModel tableProcess) {
        return new TableProcessDto()
                .setProcessUuid(tableProcess.getProcessUuid())
                .setPid(tableProcess.getPid())
                .setTableName(tableProcess.getTableName())
                .setSuccessRowsCount(tableProcess.getSuccessRowsCount())
                .setTotalRowsCount(tableProcess.getTotalRowsCount())
                .setSuccessRowsPercent(tableProcess.getSuccessRowsPercent())
                .setProcessState(tableProcess.getProcessState().toString())
                .setErrorMessage(tableProcess.getErrorMessage())
                .setCompletedDate(tableProcess.getCompletedDate());
    }
}
