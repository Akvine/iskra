package ru.akvine.iskra.services.integration.istochnik;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.TableConfig;
import ru.akvine.compozit.commons.istochnik.GenerateTableRequest;
import ru.akvine.iskra.exceptions.IntegrationException;

@Service
@RequiredArgsConstructor
public class IstochnikService {
    private final IstochnikClient client;
    private final IstochnikDtoConverter converter;

    public byte[] generatedData(TableConfig config) {
        GenerateTableRequest request = converter.convertToGenerateTableRequest(config);
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
