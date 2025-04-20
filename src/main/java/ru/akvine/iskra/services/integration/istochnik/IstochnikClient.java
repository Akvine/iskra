package ru.akvine.iskra.services.integration.istochnik;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.akvine.compozit.commons.istochnik.GenerateTableRequest;
import ru.akvine.iskra.constants.ServiceTypeConstants;

@FeignClient(value = ServiceTypeConstants.ISTOCHNIK)
public interface IstochnikClient {
    @PostMapping(value = "/generator")
    byte[] generate(@RequestBody @Valid GenerateTableRequest request);
}
