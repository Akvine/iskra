package ru.akvine.iskra.services.integration.visor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.ConnectionRequest;
import ru.akvine.compozit.commons.GenerateClearScriptRequest;
import ru.akvine.compozit.commons.GenerateClearScriptResponse;
import ru.akvine.compozit.commons.enums.DeleteMode;
import ru.akvine.compozit.commons.iskra.InsertValuesRequest;
import ru.akvine.compozit.commons.scripts.ExecuteScriptsRequest;
import ru.akvine.iskra.enums.ConstraintType;
import ru.akvine.iskra.exceptions.IntegrationException;
import ru.akvine.iskra.services.domain.connection.ConnectionModel;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.integration.visor.dto.ColumnMetadataDto;
import ru.akvine.iskra.services.integration.visor.dto.GetColumnsRequest;
import ru.akvine.iskra.services.integration.visor.dto.ListConstraintsRequest;
import ru.akvine.iskra.services.integration.visor.dto.TableMetadataDto;

import java.util.Collection;
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
        ConnectionRequest request = visorDtoConverter.convertToConnectionRequest(connection);
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
        ConnectionRequest connectionRequest = visorDtoConverter.convertToConnectionRequest(connection);
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
        ConnectionRequest connectionRequest = visorDtoConverter.convertToConnectionRequest(connection);
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

    public String executeScripts(Collection<String> scripts, ConnectionModel connection) {
        ExecuteScriptsRequest request = visorDtoConverter.convertToExecuteScriptsRequest(connection, scripts);

        try {
            visorClient.execute(request);
            return "OK";
        } catch (Exception exception) {
            String message = String.format(
                    "Error while send request to Visor. Message = [%s]",
                    exception.getMessage());
            throw new IntegrationException(message);
        }
    }

    public String generateClearScript(String tableName, DeleteMode deleteMode, ConnectionModel connection) {
        GenerateClearScriptRequest request = visorDtoConverter.convertToGenerateClearScriptRequest(
                tableName,
                deleteMode,
                connection);

        try {
            GenerateClearScriptResponse response = visorClient.generateClearScript(request);
            return response.getResult();
        } catch (Exception exception) {
            String message = String.format(
                    "Error while send request to Visor. Message = [%s]",
                    exception.getMessage());
            throw new IntegrationException(message);
        }
    }
}
