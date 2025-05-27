package com.armin.database.pushnotification.entity;

import lombok.Getter;

@Getter
public enum PushNotificationType {
    UNICAST,
    MULTICAST,
    BROADCAST;
}
