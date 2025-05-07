package ru.akvine.iskra.rest.converter;

import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.ConnectionDto;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.enums.DatabaseType;
import ru.akvine.iskra.rest.dto.connection.ConnectionResponse;
import ru.akvine.iskra.rest.dto.connection.CreateConnectionRequest;
import ru.akvine.iskra.services.domain.ConnectionModel;
import ru.akvine.iskra.services.dto.connection.CreateConnection;

import java.util.List;

@Component
public class ConnectionConverter {
    public CreateConnection convertToCreateConnection(CreateConnectionRequest request) {
        Asserts.isNotNull(request);
        return new CreateConnection()
                .setConnectionName(request.getConnectionName())
                .setDatabaseName(request.getDatabaseName())
                .setHost(request.getHost())
                .setPort(request.getPort())
                .setSchema(request.getSchema())
                .setUsername(request.getUsername())
                .setPassword(request.getPassword())
                .setDatabaseType(DatabaseType.from(request.getDatabaseType()));
    }

    public ConnectionResponse convertToConnectionResponse(List<ConnectionModel> connections) {
        return new ConnectionResponse().setConnections(connections.stream().map(this::buildConnectionDto).toList());
    }

    private ConnectionDto buildConnectionDto(ConnectionModel connection) {
        return new ConnectionDto()
                .setConnectionName(connection.getConnectionName())
                .setDatabaseName(connection.getDatabaseName())
                .setSchema(connection.getSchema())
                .setHost(connection.getHost())
                .setPort(connection.getPort())
                .setDatabaseType(connection.getDatabaseType().toString());
    }
}
