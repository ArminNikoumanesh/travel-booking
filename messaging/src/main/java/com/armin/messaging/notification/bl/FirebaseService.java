package com.armin.messaging.notification.bl;//package com.armin.messaging.notification.bl;
//
//import com.armin.security.statics.constants.ClientType;
//import com.armin.utility.object.SystemException;
//import com.google.firebase.messaging.*;
//import com.armin.database.user.repository.service.BaseUserSessionService;
//import com.armin.messaging.notification.dto.PushNotification;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class FirebaseService implements IPushNotificationService<PushNotification> {
//
//    private final FirebaseMessaging firebaseMessaging;
//    private final BaseUserSessionService userSessionService;
//
//
//    @Autowired
//    public FirebaseService(FirebaseMessaging firebaseMessaging, BaseUserSessionService userSessionService) {
//        this.firebaseMessaging = firebaseMessaging;
//        this.userSessionService = userSessionService;
//    }
//
//
//    @Override
//    public void sendMulticastPushNotification(List<Integer> userIds, List<ClientType> clientTypes, PushNotification model) throws SystemException {
//        List<String> fireBaseTokens = userSessionService.getFirebaseTokenByUserIdsAndClientTypes(userIds, clientTypes);
//        if (fireBaseTokens != null && !fireBaseTokens.isEmpty()) {
//
//            List<Message> messages = new ArrayList<>();
//            for (String token : fireBaseTokens) {
//                Message message = Message
//                        .builder()
//                        .setToken(token)
//                        .setWebpushConfig(WebpushConfig.builder().setNotification(WebpushNotification.builder().setSilent(model.isSilent()).setImage(model.getImage()).setIcon(model.getIcon()).setTitle(model.getSubject()).setBody(model.getContent()).build()).build())
//                        .setAndroidConfig(AndroidConfig.builder().setTtl(model.getTtl()).setNotification(AndroidNotification.builder().setImage(model.getImage()).setTitle(model.getSubject()).setBody(model.getContent()).setDefaultSound(model.isSilent()).setIcon(model.getIcon()).build()).build())
//                        .putAllData(model.getData())
//                        .build();
//                messages.add(message);
//            }
//            firebaseMessaging.sendAllAsync(messages);
//        }
//    }
//
//    @Override
//    public void sendBroadcastPushNotification(String topic, PushNotification model) throws SystemException {
//        Notification notification = Notification
//                .builder()
//                .setTitle(model.getSubject())
//                .setBody(model.getContent())
//                .setImage(model.getImage())
//                .build();
//
//        Message message = Message
//                .builder()
//                .setWebpushConfig(WebpushConfig.builder().setNotification(WebpushNotification.builder().setSilent(model.isSilent()).setImage(model.getImage()).setIcon(model.getIcon()).setTitle(model.getSubject()).setBody(model.getContent()).build()).build())
//                .setAndroidConfig(AndroidConfig.builder().setTtl(model.getTtl()).setNotification(AndroidNotification.builder().setImage(model.getImage()).setTitle(model.getSubject()).setBody(model.getContent()).setDefaultSound(model.isSilent()).setIcon(model.getIcon()).build()).build())
//                .setTopic(topic)
//                .setNotification(notification)
//                .putAllData(model.getData())
//                .build();
//        firebaseMessaging.sendAsync(message);
//    }
//
//    @Override
//    public void send(String topic, List<Integer> userIds, List<ClientType> clientTypes, PushNotification model) throws SystemException {
//        if (topic != null) {
//            sendBroadcastPushNotification(topic, model);
//        } else {
//            sendMulticastPushNotification(userIds, clientTypes, model);
//        }
//    }
//}
