package ru.akvine.iskra.rest.dto.regex;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;

@Data
@Accessors(chain = true)
public class ListRegexResponse extends SuccessfulResponse {
    private List<RegexDto> regexps = new ArrayList<>();
}
