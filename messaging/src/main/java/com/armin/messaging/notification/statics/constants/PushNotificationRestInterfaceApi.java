package com.armin.messaging.notification.statics.constants;

import lombok.Getter;

@Getter
public abstract class PushNotificationRestInterfaceApi {
    private PushNotificationRestInterfaceApi() {
    }

    private static final String FIREBASE = "/firebase/";
    public static final String FIRE_BASE_MULTICAST = FIREBASE + "multicast";
    public static final String FIRE_BASE_BROAD_CAST = FIREBASE + "broadcast";
}
