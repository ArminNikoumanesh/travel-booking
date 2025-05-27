package com.armin.messaging.notification.dto;

import com.armin.utility.object.INotification;
import com.armin.database.pushnotification.entity.PushNotificationLogEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class PushNotification implements INotification {
    @NotNull
    @Size(max = 140)
    private String subject;
    @NotNull
    @Size(max = 1024)
    private String content;
    private Map<String, String> data = new HashMap<>();
    private String image;
    private boolean silent;
    private String icon;
    private Long ttl;

    public PushNotification(PushNotificationLogEntity entity) {
        if (entity != null) {
            this.subject = entity.getSubject();
            this.content = entity.getContent();
            this.image = entity.getImage();
            this.icon = entity.getIcon();
            this.ttl = entity.getTtl();
            this.silent = entity.isSilent();
        }
    }
}
