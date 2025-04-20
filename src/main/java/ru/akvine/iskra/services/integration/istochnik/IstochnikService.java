package ru.akvine.iskra.services.integration.istochnik;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.TableConfig;

@Service
@RequiredArgsConstructor
public class IstochnikService {
    private final IstochnikClient client;

    public byte[] generatedData(TableConfig config) {
        return client.generate(null);
    }
}
