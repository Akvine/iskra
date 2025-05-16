package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.exceptions.connection.ConnectionNotFoundException;
import ru.akvine.iskra.repositories.ConnectionRepository;
import ru.akvine.iskra.repositories.entities.ConnectionEntity;
import ru.akvine.iskra.services.ConnectionService;
import ru.akvine.iskra.services.domain.ConnectionModel;
import ru.akvine.iskra.services.dto.connection.CreateConnection;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {
    private final ConnectionRepository connectionRepository;

    @Override
    public List<ConnectionModel> list() {
        return connectionRepository.findAll().stream()
                .map(ConnectionModel::new)
                .toList();
    }

    @Override
    public ConnectionModel create(CreateConnection connection) {
        Asserts.isNotNull(connection);
        ConnectionEntity connectionEntity = new ConnectionEntity()
                .setConnectionName(connection.getConnectionName())
                .setHost(connection.getHost())
                .setPort(connection.getPort())
                .setSchema(connection.getSchema())
                .setPassword(connection.getPassword())
                .setUsername(connection.getUsername())
                .setDatabaseName(connection.getDatabaseName())
                .setDatabaseType(connection.getDatabaseType());
        return new ConnectionModel(connectionRepository.save(connectionEntity));
    }

    @Override
    public ConnectionModel get(String connectionName) {
        return new ConnectionModel(verifyExists(connectionName));
    }

    @Override
    public ConnectionEntity verifyExists(String connectionName) {
        Asserts.isNotNull(connectionName);
        return connectionRepository
                .findByConnectionName(connectionName)
                .orElseThrow(() ->
                        new ConnectionNotFoundException("Connection with name = [" + connectionName + "] not found!"));
    }
}
