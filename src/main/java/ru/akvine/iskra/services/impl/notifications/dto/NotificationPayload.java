package ru.akvine.iskra.services.impl.notifications.dto;

import lombok.Getter;

@Getter
public class NotificationPayload {
    /**
     * Unique identifier field. For example: email, telegram username, phone and etc.
     */
    private final String target;

    public NotificationPayload(String target) {
        this.target = target;
    }
}
