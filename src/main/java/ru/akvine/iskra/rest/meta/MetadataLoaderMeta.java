package ru.akvine.iskra.rest.meta;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.akvine.compozit.commons.dto.Response;

@RequestMapping(value = "/loader")
public interface MetadataLoaderMeta {

    @GetMapping(value = "/tables-columns/start")
    Response loadOrList(@RequestParam("planUuid") String planUuid);

    @GetMapping(value = "/relations-matrix/generate")
    Response generate(@RequestParam("planUuid") String planUuid);
}
