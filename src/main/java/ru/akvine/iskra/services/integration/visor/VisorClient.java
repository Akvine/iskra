package ru.akvine.iskra.services.integration.visor;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.akvine.compozit.commons.iskra.InsertValuesRequest;
import ru.akvine.iskra.constants.ServiceTypeConstants;

@FeignClient(value = ServiceTypeConstants.VISOR)
public interface VisorClient {
    @PostMapping(value = "/databases/data/insert")
    void insertValues(@RequestBody @Valid InsertValuesRequest request);
}
