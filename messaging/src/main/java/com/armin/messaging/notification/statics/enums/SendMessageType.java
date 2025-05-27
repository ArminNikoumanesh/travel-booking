package com.armin.messaging.notification.statics.enums;

public enum SendMessageType {

    TOPIC(100),
    TOKEN(200);

    private final Integer value;

    SendMessageType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
