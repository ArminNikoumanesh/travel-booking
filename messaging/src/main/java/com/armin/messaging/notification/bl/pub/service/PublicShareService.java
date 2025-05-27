package com.armin.messaging.notification.bl.pub.service;

import com.armin.utility.object.SystemException;
import com.armin.messaging.notification.bl.NotificationService;
import com.armin.messaging.notification.bl.pub.model.dto.ShareBodyEmailIn;
import com.armin.messaging.notification.bl.pub.model.dto.ShareUrlEmailIn;
import com.armin.messaging.notification.dto.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Service
public class PublicShareService {
    private final NotificationService notificationService;

    @Autowired
    public PublicShareService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void shareUrlByEmailAsync(ShareUrlEmailIn model) throws SystemException {
        Notification notification = new Notification.Builder("template", model)
                .setEmailConfig(model.getEmail())
                .build();
        notificationService.sendEmail(notification);
    }

    public void shareBodyByEmailAsync(ShareBodyEmailIn model) throws SystemException {
        Notification notification = new Notification.Builder("template", model)
                .setEmailConfig(model.getEmail())
                .build();
        notificationService.sendEmail(notification);
    }
}
