package ru.akvine.iskra.services.integration.visor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.*;
import ru.akvine.compozit.commons.enums.DeleteMode;
import ru.akvine.compozit.commons.iskra.InsertValuesRequest;
import ru.akvine.compozit.commons.scripts.ExecuteScriptsRequest;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.compozit.commons.visor.GenerateScriptsRequest;
import ru.akvine.compozit.commons.visor.GenerateScriptsResponse;
import ru.akvine.compozit.commons.visor.ScriptResultDto;
import ru.akvine.iskra.exceptions.IntegrationException;
import ru.akvine.iskra.services.domain.connection.ConnectionModel;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.integration.visor.dto.*;

@Service
@RequiredArgsConstructor
// TODO: вынести методы в интерфейс
public class VisorService {
    private final VisorClient visorClient;
    private final VisorDtoMapper visorDtoMapper;

    public void sendFile(TableModel tableModel, byte[] table) {
        InsertValuesRequest request = visorDtoMapper.convert(tableModel, table);
        try {
            visorClient.insertValues(request);
        } catch (Exception exception) {
            String message = String.format("Error while send request to Visor. Message = [%s]", exception.getMessage());
            throw new IntegrationException(message);
        }
    }

    public List<TableMetadataDto> loadTables(ConnectionModel connection) {
        ConnectionRequest request = visorDtoMapper.convertToConnectionRequest(connection);
        try {
            return visorClient.loadTables(request).getTables();
        } catch (Exception exception) {
            String message = String.format("Error while send request to Visor. Message = [%s]", exception.getMessage());
            throw new IntegrationException(message);
        }
    }

    public List<ColumnMetadataDto> loadColumns(String tableName, ConnectionModel connection) {
        ConnectionRequest connectionRequest = visorDtoMapper.convertToConnectionRequest(connection);
        GetColumnsRequest request =
                new GetColumnsRequest().setConnection(connectionRequest).setTableName(tableName);
        try {
            return visorClient.loadColumns(request).getColumns();
        } catch (Exception exception) {
            String message = String.format("Error while send request to Visor. Message = [%s]", exception.getMessage());
            throw new IntegrationException(message);
        }
    }

    public LoadConstraintsResult loadConstraints(String tableName, String columnName, ConnectionModel connection) {
        ConnectionRequest connectionRequest = visorDtoMapper.convertToConnectionRequest(connection);
        ListConstraintsRequest request = new ListConstraintsRequest()
                .setConnectionInfo(connectionRequest)
                .setColumnName(columnName)
                .setTableName(tableName);
        try {
            return visorDtoMapper.convertToLoadConstraintsResult(visorClient.loadConstraints(request));
        } catch (Exception exception) {
            String message = String.format("Error while send request to Visor. Message = [%s]", exception.getMessage());
            throw new IntegrationException(message);
        }
    }

    public String executeScripts(Collection<String> scripts, ConnectionModel connection) {
        ExecuteScriptsRequest request = visorDtoMapper.convertToExecuteScriptsRequest(connection, scripts);

        try {
            visorClient.execute(request);
            return "OK";
        } catch (Exception exception) {
            String message = String.format("Error while send request to Visor. Message = [%s]", exception.getMessage());
            throw new IntegrationException(message);
        }
    }

    public String generateClearScript(String tableName, DeleteMode deleteMode, ConnectionModel connection) {
        GenerateClearScriptRequest request =
                visorDtoMapper.convertToGenerateClearScriptRequest(tableName, deleteMode, connection);

        try {
            GenerateClearScriptResponse response = visorClient.generateClearScript(request);
            return response.getResult();
        } catch (Exception exception) {
            String message = String.format("Error while send request to Visor. Message = [%s]", exception.getMessage());
            throw new IntegrationException(message);
        }
    }

    public List<String> getRelatedTables(String tableName, ConnectionModel connection) {
        GetRelatedTablesRequest request = visorDtoMapper.convertToGetRelatedTablesRequest(tableName, connection);

        try {
            GetRelatedTablesResponse response = visorClient.getRelatedTables(request);
            return response.getRelatedTablesNames();
        } catch (Exception exception) {
            String message = String.format("Error while send request to Visor. Message = [%s]", exception.getMessage());
            throw new IntegrationException(message);
        }
    }

    public Map<String, ScriptResultDto> generateScriptsForTables(
            Collection<String> tableNames, List<String> constraints, ConnectionModel connection) {
        Asserts.isNotEmpty(tableNames);
        Asserts.isNotEmpty(constraints);

        GenerateScriptsRequest request =
                visorDtoMapper.convertToGenerateScriptsRequest(tableNames, constraints, connection);

        try {
            GenerateScriptsResponse response = visorClient.generateScripts(request);
            return response.getTableNameWithScripts();
        } catch (Exception exception) {
            String message = String.format("Error while send request to Visor. Message = [%s]", exception.getMessage());
            throw new IntegrationException(message);
        }
    }
}
