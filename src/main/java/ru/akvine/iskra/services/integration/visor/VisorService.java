package ru.akvine.iskra.services.integration.visor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.ConnectionDto;
import ru.akvine.compozit.commons.TableConfig;
import ru.akvine.compozit.commons.iskra.InsertValuesRequest;

@Service
@RequiredArgsConstructor
public class VisorService {
    private final VisorClient visorClient;
    private final VisorDtoConverter visorDtoConverter;

    public void sendFile(byte[] table, TableConfig config, ConnectionDto connection) {
        InsertValuesRequest request = visorDtoConverter.convert(table, config, connection);
        visorClient.insertValues(request);
    }
}
