package ru.akvine.iskra.services.domain.connection;

import java.util.List;
import ru.akvine.iskra.repositories.entities.ConnectionEntity;
import ru.akvine.iskra.services.domain.connection.dto.CreateConnection;
import ru.akvine.iskra.services.domain.connection.dto.DuplicateConnection;

public interface ConnectionService {
    List<ConnectionModel> list(String userUuid);

    ConnectionModel create(CreateConnection connection);

    ConnectionModel duplicate(DuplicateConnection action);

    ConnectionModel get(String connectionName, String userUuid);

    ConnectionEntity verifyExists(String connectionName, String userUuid);
}
