package ru.akvine.iskra.services;

import ru.akvine.iskra.enums.NotificationServiceType;
import ru.akvine.iskra.services.impl.notifications.dto.NotificationPayload;

public interface NotificationService<P extends NotificationPayload> {

    boolean notify(P payload);

    NotificationServiceType getType();
}
