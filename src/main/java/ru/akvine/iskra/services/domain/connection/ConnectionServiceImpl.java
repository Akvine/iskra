package ru.akvine.iskra.services.domain.connection;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.components.NameGenerator;
import ru.akvine.iskra.exceptions.connection.ConnectionNotFoundException;
import ru.akvine.iskra.repositories.ConnectionRepository;
import ru.akvine.iskra.repositories.entities.ConnectionEntity;
import ru.akvine.iskra.repositories.entities.UserEntity;
import ru.akvine.iskra.services.UserService;
import ru.akvine.iskra.services.domain.connection.dto.CreateConnection;
import ru.akvine.iskra.services.domain.connection.dto.DuplicateConnection;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {
    private final ConnectionRepository connectionRepository;
    private final UserService userService;
    private final NameGenerator nameGenerator;

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
    public ConnectionModel duplicate(DuplicateConnection action) {
        Asserts.isNotNull(action);

        String userUuid = action.getUserUuid();
        UserEntity owner = userService.verifyExistsByUuid(userUuid);
        ConnectionEntity from = verifyExists(action.getConnectionName(), userUuid);
        ConnectionEntity target = new ConnectionEntity()
                .setHost(from.getHost())
                .setPort(from.getPort())
                .setUsername(from.getUsername())
                .setPassword(from.getPassword())
                .setDatabaseName(from.getDatabaseName())
                .setSchema(from.getSchema())
                .setDatabaseType(from.getDatabaseType())
                .setUser(owner);

        if (StringUtils.isNotBlank(action.getNewConnectionName()) &&
                !action.getNewConnectionName().equals(from.getConnectionName())) {
            target.setConnectionName(action.getNewConnectionName());
        } else {
            List<String> generatedNames = nameGenerator.tryGetIncrementedNames(from.getConnectionName(), 1);
            if (CollectionUtils.isEmpty(generatedNames)) {
                target.setConnectionName(from.getConnectionName() + "_1");
            } else {
                target.setConnectionName(generatedNames.getFirst());
            }

        }

        return new ConnectionModel(connectionRepository.save(target));
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
