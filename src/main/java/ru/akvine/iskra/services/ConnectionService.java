package ru.akvine.iskra.services;

import ru.akvine.iskra.repositories.entities.ConnectionEntity;
import ru.akvine.iskra.services.domain.ConnectionModel;
import ru.akvine.iskra.services.dto.connection.CreateConnection;

import java.util.List;

public interface ConnectionService {
    List<ConnectionModel> list();

    ConnectionModel create(CreateConnection connection);

    ConnectionModel get(String connectionName);

    ConnectionEntity verifyExists(String connectionName);
}
