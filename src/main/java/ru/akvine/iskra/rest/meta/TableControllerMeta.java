package ru.akvine.iskra.rest.meta;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.dto.table.ListTablesRequest;
import ru.akvine.iskra.rest.dto.table.ToggleSelectedRequest;

@RequestMapping(value = "/tables")
public interface TableControllerMeta {

    @GetMapping
    Response list(@RequestBody @Valid ListTablesRequest request);

    @PatchMapping(value = "/toggle")
    Response toggleSelected(@RequestBody @Valid ToggleSelectedRequest request);
}
