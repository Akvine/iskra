package ru.akvine.iskra.services.integration.visor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.ConnectionDto;
import ru.akvine.compozit.commons.TableConfig;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.compozit.commons.iskra.InsertValuesRequest;
import ru.akvine.iskra.exceptions.IntegrationException;
import ru.akvine.iskra.services.domain.ConnectionModel;
import ru.akvine.iskra.services.dto.TableMetadataDto;
import ru.akvine.iskra.services.integration.visor.dto.ConnectionRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisorService {
    private final VisorClient visorClient;
    private final VisorDtoConverter visorDtoConverter;

    public void sendFile(TableName tableName, byte[] table, TableConfig config, ConnectionDto connection) {
        InsertValuesRequest request = visorDtoConverter.convert(tableName, table, config, connection);
        try {
            visorClient.insertValues(request);
        } catch (Exception exception) {
            String message = String.format(
                    "Error while send request to Visor. Message = [%s]",
                    exception.getMessage());
            throw new IntegrationException(message);
        }
    }

    public List<TableMetadataDto> loadTables(ConnectionModel connection) {
        ConnectionRequest request = visorDtoConverter.convert(connection);
        try {
           return visorClient.loadTables(request).getTables();
        } catch (Exception exception) {
            String message = String.format(
                    "Error while send request to Visor. Message = [%s]",
                    exception.getMessage());
            throw new IntegrationException(message);
        }
    }
}
