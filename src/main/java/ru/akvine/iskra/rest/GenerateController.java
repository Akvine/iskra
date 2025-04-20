package ru.akvine.iskra.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.GenerateDataRequest;
import ru.akvine.iskra.rest.converter.GenerateConverter;
import ru.akvine.iskra.rest.meta.GenerateControllerMeta;
import ru.akvine.iskra.services.GenerateDataFacade;
import ru.akvine.iskra.services.dto.GenerateDataAction;

@RestController
@RequiredArgsConstructor
public class GenerateController implements GenerateControllerMeta {
    private final GenerateDataFacade generateDataFacade;
    private final GenerateConverter generateConverter;

    @Override
    public ResponseEntity<?> start(@RequestBody @Valid GenerateDataRequest request) {
        GenerateDataAction action = generateConverter.convertToGenerateDataAction(request);
        generateDataFacade.generateData(action);
        return ResponseEntity.ok("SUCCESS");
    }
}
