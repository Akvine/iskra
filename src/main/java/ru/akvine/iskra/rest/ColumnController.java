package ru.akvine.iskra.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.converter.ColumnConverter;
import ru.akvine.iskra.rest.dto.column.SelectColumnRequest;
import ru.akvine.iskra.rest.meta.ColumnControllerMeta;
import ru.akvine.iskra.services.ColumnService;
import ru.akvine.iskra.services.domain.ColumnModel;
import ru.akvine.iskra.services.dto.column.SelectColumn;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ColumnController implements ColumnControllerMeta {
    private final ColumnConverter columnConverter;
    private final ColumnService columnService;

    @Override
    public Response select(@RequestBody @Valid SelectColumnRequest request) {
        SelectColumn selectColumn = columnConverter.convertToSelectColumn(request);
        List<ColumnModel> selected = columnService.selectAll(selectColumn);
        return columnConverter.convertToListColumnResponse(selected);
    }
}
