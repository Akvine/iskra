package ru.akvine.iskra.rest.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.ConnectionDto;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.components.SecurityManager;
import ru.akvine.iskra.enums.DatabaseType;
import ru.akvine.iskra.rest.dto.connection.ConnectionResponse;
import ru.akvine.iskra.rest.dto.connection.CreateConnectionRequest;
import ru.akvine.iskra.rest.dto.connection.DuplicateConnectionRequest;
import ru.akvine.iskra.services.domain.connection.ConnectionModel;
import ru.akvine.iskra.services.dto.connection.CreateConnection;
import ru.akvine.iskra.services.dto.connection.DuplicateConnection;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConnectionMapper {
    private final SecurityManager securityManager;

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
                .setUserUuid(securityManager.getCurrentUser().getUuid())
                .setDatabaseType(DatabaseType.from(request.getDatabaseType()));
    }

    public DuplicateConnection convertToDuplicateConnection(DuplicateConnectionRequest request) {
        return new DuplicateConnection()
                .setUserUuid(securityManager.getCurrentUser().getUuid())
                .setConnectionName(request.getConnectionName())
                .setNewConnectionName(request.getNewConnectionName());
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
