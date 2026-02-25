package ru.akvine.iskra.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class LoadMetadataEvent extends ApplicationEvent {
    private final String planUuid;
    private final String userUuid;

    public LoadMetadataEvent(Object source, String planUuid, String userUuid) {
        super(source);
        this.planUuid = planUuid;
        this.userUuid = userUuid;
    }
}
