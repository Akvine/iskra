package ru.akvine.iskra.rest.dto.dictionary;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;

@Data
@Accessors(chain = true)
public class DictionaryListResponse extends SuccessfulResponse {
    private List<DictionaryDto> dictionaries;
}
