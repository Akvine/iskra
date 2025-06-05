package ru.akvine.iskra.rest.dto.dictionary;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DictionaryDto {
    private String uuid;
    private String name;
    private String language;
    private String description;
    private String values;
    private boolean system;
    private int elements;
}
