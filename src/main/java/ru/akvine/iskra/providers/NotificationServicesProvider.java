package ru.akvine.iskra.providers;

import ru.akvine.iskra.enums.NotificationServiceType;
import ru.akvine.iskra.services.NotificationService;
import ru.akvine.iskra.services.impl.notifications.dto.NotificationPayload;

import java.util.Map;

public record NotificationServicesProvider(
        Map<NotificationServiceType, NotificationService<? extends NotificationPayload>> notificationServices) {
    public NotificationService getByType(NotificationServiceType type) {
        if (notificationServices.containsKey(type)) {
            return notificationServices.get(type);
        }

        throw new UnsupportedOperationException("Notification service with type = [" + type + "] is not supported by app!");
    }
}
