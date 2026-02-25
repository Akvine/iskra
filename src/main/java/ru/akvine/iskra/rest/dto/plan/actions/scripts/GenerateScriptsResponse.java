package ru.akvine.iskra.rest.dto.plan.actions.scripts;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;

@Data
@Accessors(chain = true)
public class GenerateScriptsResponse extends SuccessfulResponse {
    private List<String> dropScripts = List.of();
    private List<String> createScripts = List.of();
    private List<String> clearScripts = List.of();
}
