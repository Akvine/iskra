package ru.akvine.iskra.rest.dto.regex;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class ListRegexResponse extends SuccessfulResponse {
    private List<RegexDto> regexps = new ArrayList<>();
}
