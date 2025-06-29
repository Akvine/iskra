package ru.akvine.iskra.rest.dto.plan;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ConstraintsSettingsInfo {
    private boolean generateScriptsForNotNull;
    private boolean generateScriptsForIndex;
    private boolean generateScriptsForPrimaryKey;
    private boolean generateScriptsForTrigger;
    private boolean generateScriptsForUnique;
    private boolean generateScriptsForCheck;
    private boolean generateScriptsForDefault;
}
