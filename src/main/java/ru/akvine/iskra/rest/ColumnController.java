package ru.akvine.iskra.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.mappers.ColumnMapper;
import ru.akvine.iskra.rest.dto.column.SelectColumnRequest;
import ru.akvine.iskra.rest.meta.ColumnControllerMeta;
import ru.akvine.iskra.services.domain.column.ColumnService;
import ru.akvine.iskra.services.domain.column.ColumnModel;
import ru.akvine.iskra.services.domain.column.dto.SelectColumn;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ColumnController implements ColumnControllerMeta {
    private final ColumnMapper columnMapper;
    private final ColumnService columnService;

    @Override
    public Response select(@RequestBody @Valid SelectColumnRequest request) {
        SelectColumn selectColumn = columnMapper.mapToSelectColumn(request);
        List<ColumnModel> selected = columnService.selectAll(selectColumn);
        return columnMapper.mapToListColumnResponse(selected);
    }
}
