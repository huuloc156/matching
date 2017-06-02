package com.rentracks.matching.fragment.events;

/**
 * Created by apple on 2/5/16.
 */
public class PushNotificationEvent {
    public final String value;
    public final NotificationType type;
    public PushNotificationEvent(NotificationType type, String value) {
        this.value = value;
        this.type = type;
    }
    public enum NotificationType{
        COMMENT,FOLLOWED
    }
}
