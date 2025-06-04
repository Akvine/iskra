package ru.akvine.iskra.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.components.SecurityManager;
import ru.akvine.iskra.rest.mappers.ConnectionMapper;
import ru.akvine.iskra.rest.dto.connection.CreateConnectionRequest;
import ru.akvine.iskra.rest.meta.ConnectionControllerMeta;
import ru.akvine.iskra.services.domain.connection.ConnectionService;
import ru.akvine.iskra.services.domain.connection.ConnectionModel;
import ru.akvine.iskra.services.dto.connection.CreateConnection;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConnectionController implements ConnectionControllerMeta {
    private final ConnectionMapper connectionMapper;
    private final ConnectionService connectionService;
    private final SecurityManager securityManager;

    @Override
    public Response list() {
        List<ConnectionModel> connections = connectionService.list(securityManager.getCurrentUser().getUuid());
        return connectionMapper.convertToConnectionResponse(connections);
    }

    @Override
    public Response create(@RequestBody @Valid CreateConnectionRequest request) {
        CreateConnection createConnection = connectionMapper.convertToCreateConnection(request);
        ConnectionModel createdConnection = connectionService.create(createConnection);
        return connectionMapper.convertToConnectionResponse(List.of(createdConnection));
    }
}
