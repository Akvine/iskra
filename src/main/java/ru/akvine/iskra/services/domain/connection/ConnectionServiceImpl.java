package ru.akvine.iskra.services.domain.connection;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.exceptions.connection.ConnectionNotFoundException;
import ru.akvine.iskra.repositories.ConnectionRepository;
import ru.akvine.iskra.repositories.entities.ConnectionEntity;
import ru.akvine.iskra.repositories.entities.UserEntity;
import ru.akvine.iskra.services.UserService;
import ru.akvine.iskra.services.dto.connection.CreateConnection;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {
    private final ConnectionRepository connectionRepository;
    private final UserService userService;

    @Override
    @Transactional
    public List<ConnectionModel> list(String userUuid) {
        Asserts.isNotBlank(userUuid, "userUuid is blank");
        return connectionRepository.findAll(userUuid).stream()
                .map(ConnectionModel::new)
                .toList();
    }

    @Override
    public ConnectionModel create(CreateConnection connection) {
        Asserts.isNotNull(connection);

        UserEntity owner = userService.verifyExistsByUuid(connection.getUserUuid());
        ConnectionEntity connectionEntity = new ConnectionEntity()
                .setConnectionName(connection.getConnectionName())
                .setHost(connection.getHost())
                .setPort(connection.getPort())
                .setSchema(connection.getSchema())
                .setPassword(connection.getPassword())
                .setUsername(connection.getUsername())
                .setDatabaseName(connection.getDatabaseName())
                .setDatabaseType(connection.getDatabaseType())
                .setUser(owner);
        return new ConnectionModel(connectionRepository.save(connectionEntity));
    }

    @Override
    public ConnectionModel get(String connectionName, String userUuid) {
        return new ConnectionModel(verifyExists(connectionName, userUuid));
    }

    @Override
    public ConnectionEntity verifyExists(String connectionName, String userUuid) {
        Asserts.isNotBlank(connectionName, "connectionName is blank!");
        Asserts.isNotBlank(userUuid, "userUuid is blank!");
        return connectionRepository
                .findByConnectionName(connectionName, userUuid)
                .orElseThrow(() ->
                        new ConnectionNotFoundException("Connection with name = [" + connectionName + "] not found!"));
    }
}
