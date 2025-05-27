package com.armin.database.pushnotification.entity;

public enum PushNotificationServiceType {

    FIREBASE(100),
    CHESHMAK(200);

    private final Integer value;

    PushNotificationServiceType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
