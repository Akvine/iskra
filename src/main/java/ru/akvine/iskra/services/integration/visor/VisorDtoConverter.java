package ru.akvine.iskra.services.integration.visor;

import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.*;
import ru.akvine.compozit.commons.iskra.InsertValuesRequest;
import ru.akvine.iskra.services.domain.ConnectionModel;
import ru.akvine.iskra.services.integration.visor.dto.ConnectionRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VisorDtoConverter {
    public InsertValuesRequest convert(TableName tableName,
                                       byte[] table,
                                       TableConfig config,
                                       ConnectionDto connection) {
        return new InsertValuesRequest()
                .setTableName(tableName.getName())
                .setContent(table)
                .setConnection(connection)
                .setColumnsMetaInfo(buildColumnsMetaInfo(config.getColumnConfigs()));

    }

    public ConnectionRequest convert(ConnectionModel connection) {
        return new ConnectionRequest()
                .setDatabaseName(connection.getDatabaseName())
                .setHost(connection.getHost())
                .setPort(connection.getPort())
                .setSchema(connection.getSchema())
                .setUsername(connection.getUsername())
                .setPassword(connection.getPassword())
                .setDatabaseType(connection.getDatabaseType().getValue());
    }

    private Map<String, ColumnMetaInfoDto> buildColumnsMetaInfo(List<ColumnInfo> columnConfigs) {
        Map<String, ColumnMetaInfoDto> metaInfo = new HashMap<>();
        columnConfigs.forEach(config -> metaInfo.put(config
                .getColumn()
                .getName(), config.getColumnMetaInfo()));
        return metaInfo;
    }
}
