package ru.akvine.iskra.rest.meta;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.akvine.compozit.commons.dto.Response;

@RequestMapping(value = "/tables")
public interface TableControllerMeta {

    @PostMapping
    Response createOrListIfExist(@RequestParam("planUuid") String planUuid);
}
