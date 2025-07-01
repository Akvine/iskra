package ru.akvine.iskra.services.domain.plan.dto.action;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class GenerateScriptsResult {
    private List<String> dropScripts = new ArrayList<>();
    private List<String> createScripts = new ArrayList<>();
    private List<String> clearScripts = new ArrayList<>();
}
