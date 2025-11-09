package ru.akvine.iskra.repositories.entities.embaddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.RelationShipType;

@Embeddable
@Accessors(chain = true)
@Getter
@Setter
public class ReferenceInfo {
    @Column(name = "TARGET_COLUMN_NAME_FOR_FOREIGN_KEY")
    private String targetColumnNameForForeignKey;

    @Column(name = "TARGET_TABLE_NAME_FOR_FOREIGN_KEY")
    private String targetTableNameForForeignKey;

    @Column(name = "RELATION_SHIP_TYPE")
    @Enumerated(EnumType.STRING)
    private RelationShipType relationShipType;
}
