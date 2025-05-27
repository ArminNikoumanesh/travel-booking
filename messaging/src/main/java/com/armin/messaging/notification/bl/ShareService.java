package com.armin.messaging.notification.bl;

import com.armin.utility.object.SystemException;
import com.armin.messaging.notification.dto.Notification;
import com.armin.messaging.notification.dto.ShareBodyEmailIn;
import com.armin.messaging.notification.dto.ShareEmailIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Service
public class ShareService {

    private final NotificationService notificationService;

    @Autowired
    public ShareService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void shareUrlByEmailAsync(ShareEmailIn model) throws SystemException {
        Notification notification = new Notification.Builder("template", model)
                .setEmailConfig(model.getEmail())
                .build();
        notificationService.sendEmail(notification);
    }

    public void shareContentByEmailAsync(ShareBodyEmailIn model) throws SystemException {
        Notification notification = new Notification.Builder("template", model)
                .setEmailConfig(model.getEmail())
                .build();
        notificationService.sendEmail(notification);
    }

}
