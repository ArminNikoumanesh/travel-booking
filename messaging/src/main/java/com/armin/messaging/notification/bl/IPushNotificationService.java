package com.armin.messaging.notification.bl;


import com.armin.security.statics.constants.ClientType;
import com.armin.utility.object.INotification;
import com.armin.utility.object.SystemException;
import com.armin.messaging.notification.dto.PushNotification;

import java.util.List;

public interface IPushNotificationService<T extends INotification> {

    void sendMulticastPushNotification(List<Integer> userIds, List<ClientType> clientTypes, T model) throws SystemException;

    void sendBroadcastPushNotification(String topic, T model) throws SystemException;

    void send(String topic, List<Integer> userIds, List<ClientType> clientTypes, PushNotification model) throws SystemException;
}
