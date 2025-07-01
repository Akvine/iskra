package ru.akvine.iskra.services.domain.connection.dto;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DuplicateConnection {
    private String userUuid;
    private String connectionName;
    @Nullable
    private String newConnectionName;
}
