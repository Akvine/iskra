package ru.akvine.iskra.services.integration.visor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.iskra.InsertValuesRequest;
import ru.akvine.iskra.enums.ConstraintType;
import ru.akvine.iskra.exceptions.IntegrationException;
import ru.akvine.iskra.services.domain.ConnectionModel;
import ru.akvine.iskra.services.domain.TableModel;
import ru.akvine.iskra.services.integration.visor.dto.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisorService {
    private final VisorClient visorClient;
    private final VisorDtoConverter visorDtoConverter;

    public void sendFile(TableModel tableModel, byte[] table) {
        InsertValuesRequest request = visorDtoConverter.convert(tableModel, table);
        try {
            visorClient.insertValues(request);
        } catch (Exception exception) {
            String message = String.format(
                    "Error while send request to Visor. Message = [%s]",
                    exception.getMessage());
            throw new IntegrationException(message);
        }
    }

    public List<TableMetadataDto> loadTables(ConnectionModel connection) {
        ConnectionRequest request = visorDtoConverter.convert(connection);
        try {
           return visorClient.loadTables(request).getTables();
        } catch (Exception exception) {
            String message = String.format(
                    "Error while send request to Visor. Message = [%s]",
                    exception.getMessage());
            throw new IntegrationException(message);
        }
    }

    public List<ColumnMetadataDto> loadColumns(String tableName, ConnectionModel connection) {
        ConnectionRequest connectionRequest = visorDtoConverter.convert(connection);
        GetColumnsRequest request = new GetColumnsRequest()
                .setConnection(connectionRequest)
                .setTableName(tableName);
        try {
            return visorClient.loadColumns(request).getColumns();
        } catch (Exception exception) {
            String message = String.format(
                    "Error while send request to Visor. Message = [%s]",
                    exception.getMessage());
            throw new IntegrationException(message);
        }
    }

    public List<ConstraintType> loadConstraints(String tableName, String columnName, ConnectionModel connection) {
        ConnectionRequest connectionRequest = visorDtoConverter.convert(connection);
        ListConstraintsRequest request = new ListConstraintsRequest()
                .setConnectionInfo(connectionRequest)
                .setColumnName(columnName)
                .setTableName(tableName);
        try {
            return visorClient.loadConstraints(request).getConstraintTypes();
        } catch (Exception exception) {
            String message = String.format(
                    "Error while send request to Visor. Message = [%s]",
                    exception.getMessage());
            throw new IntegrationException(message);
        }
    }
}
