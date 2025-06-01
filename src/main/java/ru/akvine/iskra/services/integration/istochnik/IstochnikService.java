package ru.akvine.iskra.services.integration.istochnik;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.istochnik.GenerateTableRequest;
import ru.akvine.iskra.exceptions.IntegrationException;
import ru.akvine.iskra.services.domain.table.TableModel;

@Service
@RequiredArgsConstructor
public class IstochnikService {
    private final IstochnikClient client;
    private final IstochnikDtoMapper converter;

    public byte[] generatedData(int processedRowsCount, TableModel table) {
        GenerateTableRequest request = converter.convertToGenerateTableRequest(processedRowsCount, table);
        try {
            return client.generate(request);
        } catch (Exception exception) {
            String message = String.format(
                    "Error while send request to Istochnik. Message = [%s]",
                    exception.getMessage());
            throw new IntegrationException(message);
        }
    }
}
