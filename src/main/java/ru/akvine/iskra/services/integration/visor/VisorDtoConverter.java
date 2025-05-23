package ru.akvine.iskra.services.integration.visor;

import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.ColumnMetaInfoDto;
import ru.akvine.compozit.commons.ConnectionDto;
import ru.akvine.compozit.commons.iskra.InsertValuesRequest;
import ru.akvine.iskra.services.domain.ColumnModel;
import ru.akvine.iskra.services.domain.ConnectionModel;
import ru.akvine.iskra.services.domain.TableModel;
import ru.akvine.iskra.services.integration.visor.dto.ConnectionRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VisorDtoConverter {
    public InsertValuesRequest convert(TableModel tableModel,
                                       byte[] table) {
        return new InsertValuesRequest()
                .setTableName(tableModel.getTableName())
                .setContent(table)
                .setConnection(convertToConnectionDto(tableModel.getPlan().getConnection()))
                .setColumnsMetaInfo(buildColumnsMetaInfo(tableModel.getColumns()));

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
