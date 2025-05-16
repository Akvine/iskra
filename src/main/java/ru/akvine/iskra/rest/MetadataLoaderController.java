package ru.akvine.iskra.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.converter.TableConverter;
import ru.akvine.iskra.rest.meta.MetadataLoaderMeta;
import ru.akvine.iskra.services.MetadataLoaderService;
import ru.akvine.iskra.services.domain.TableModel;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MetadataLoaderController implements MetadataLoaderMeta {
    private final MetadataLoaderService metadataLoaderService;
    private final TableConverter tableConverter;

    @Override
    public Response loadOrList(@RequestParam("planUuid") String planUuid) {
        List<TableModel> tables = metadataLoaderService.loadOrList(planUuid);
        return tableConverter.convertToListTablesResponse(tables);
    }
}
