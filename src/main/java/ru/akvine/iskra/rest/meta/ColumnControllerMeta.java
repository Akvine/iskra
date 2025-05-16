package ru.akvine.iskra.rest.meta;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.akvine.compozit.commons.dto.Response;

@RequestMapping(value = "/columns")
public interface ColumnControllerMeta {
    @GetMapping
    Response list(@RequestParam("planUuid") String planUuid,
                  @RequestParam("tableName") String tableName);

}
