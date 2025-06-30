package ru.akvine.iskra.rest.dto.plan.actions.scripts;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;

import java.util.List;

@Data
@Accessors(chain = true)
public class GenerateScriptsResponse extends SuccessfulResponse {
    private List<String> dropScripts = List.of();
    private List<String> createScripts = List.of();
    private List<String> clearScripts = List.of();
}
