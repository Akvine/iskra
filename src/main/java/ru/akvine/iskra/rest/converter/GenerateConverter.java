package ru.akvine.iskra.rest.converter;

import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.GenerateDataRequest;
import ru.akvine.iskra.services.dto.GenerateDataAction;

@Component
public class GenerateConverter {
    public GenerateDataAction convertToGenerateDataAction(GenerateDataRequest request) {
        return new GenerateDataAction()
                .setConnection(request.getConnection())
                .setConfiguration(request.getConfiguration());
    }
}
