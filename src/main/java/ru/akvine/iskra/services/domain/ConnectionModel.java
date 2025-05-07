package ru.akvine.iskra.services.domain;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.DatabaseType;
import ru.akvine.iskra.repositories.entities.ConnectionEntity;

@Data
@Accessors(chain = true)
public class ConnectionModel extends Model<Long> {
    private String connectionName;
    @Nullable
    private String databaseName;
    @Nullable
    private String schema;
    private String host;
    private String port;
    @ToString.Exclude
    private String username;
    @ToString.Exclude
    private String password;
    private DatabaseType databaseType;

    public ConnectionModel(ConnectionEntity connection) {
        super(connection);

        this.connectionName = connection.getConnectionName();
        this.databaseName = connection.getDatabaseName();
        this.host = connection.getHost();
        this.port = connection.getPort();
        this.schema = connection.getSchema();
        this.username = connection.getUsername();
        this.password = connection.getPassword();
        this.databaseType = connection.getDatabaseType();
    }
}
