package ru.akvine.iskra.services.integration.visor;

import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.ColumnMetaInfoDto;
import ru.akvine.compozit.commons.ConnectionDto;
import ru.akvine.compozit.commons.ConnectionRequest;
import ru.akvine.compozit.commons.GenerateClearScriptRequest;
import ru.akvine.compozit.commons.enums.DeleteMode;
import ru.akvine.compozit.commons.iskra.InsertValuesRequest;
import ru.akvine.compozit.commons.scripts.ExecuteScriptsRequest;
import ru.akvine.iskra.services.domain.column.ColumnModel;
import ru.akvine.iskra.services.domain.connection.ConnectionModel;
import ru.akvine.iskra.services.domain.table.TableModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VisorDtoMapper {
    public InsertValuesRequest convert(TableModel tableModel,
                                       byte[] table) {
        return new InsertValuesRequest()
                .setTableName(tableModel.getTableName())
                .setContent(table)
                .setConnection(convertToConnectionDto(tableModel.getPlan().getConnection()))
                .setColumnsMetaInfo(buildColumnsMetaInfo(tableModel.getColumns()));

    }

    public ExecuteScriptsRequest convertToExecuteScriptsRequest(ConnectionModel connectionModel, Collection<String> scripts) {
        return new ExecuteScriptsRequest()
                .setConnection(convertToConnectionRequest(connectionModel))
                .setScripts(scripts);
    }

    public ConnectionRequest convertToConnectionRequest(ConnectionModel connection) {
        return new ConnectionRequest()
                .setDatabaseName(connection.getDatabaseName())
                .setHost(connection.getHost())
                .setPort(connection.getPort())
                .setSchema(connection.getSchema())
                .setUsername(connection.getUsername())
                .setPassword(connection.getPassword())
                .setDatabaseType(connection.getDatabaseType().getValue());
    }

    public GenerateClearScriptRequest convertToGenerateClearScriptRequest(String tableName,
                                                                          DeleteMode deleteMode,
                                                                          ConnectionModel connectionModel) {
        return new GenerateClearScriptRequest()
                .setConnection(convertToConnectionRequest(connectionModel))
                .setTableName(tableName)
                .setDeleteMode(deleteMode.getName());
    }

    private ConnectionDto convertToConnectionDto(ConnectionModel connection) {
        return new ConnectionDto()
                .setDatabaseType(connection.getDatabaseType().toString())
                .setHost(connection.getHost())
                .setPort(connection.getPort())
                .setUsername(connection.getUsername())
                .setPassword(connection.getPassword())
                .setDatabaseName(connection.getDatabaseName())
                .setConnectionName(connection.getConnectionName());
    }

    private Map<String, ColumnMetaInfoDto> buildColumnsMetaInfo(List<ColumnModel> columns) {
        Map<String, ColumnMetaInfoDto> metaInfo = new HashMap<>();

        columns.forEach(column -> {
            metaInfo.put(column.getColumnName(), new ColumnMetaInfoDto()
                    .setColumnTypeName(column.getRawDataType()));
        });
        return metaInfo;
    }
}
