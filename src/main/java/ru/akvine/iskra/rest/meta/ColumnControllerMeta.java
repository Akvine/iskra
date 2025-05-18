package ru.akvine.iskra.rest.meta;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.dto.column.SelectColumnRequest;

@RequestMapping(value = "/columns")
public interface ColumnControllerMeta {
    @PatchMapping(value = "/select")
    Response select(@RequestBody @Valid SelectColumnRequest request);

}
