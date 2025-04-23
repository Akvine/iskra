package ru.akvine.iskra.services.integration.istochnik;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.TableConfig;
import ru.akvine.compozit.commons.istochnik.GenerateTableRequest;

@Service
@RequiredArgsConstructor
public class IstochnikService {
    private final IstochnikClient client;
    private final IstochnikDtoConverter converter;

    public byte[] generatedData(TableConfig config) {
        GenerateTableRequest request = converter.convertToGenerateTableRequest(config);
        return client.generate(request);
    }
}
