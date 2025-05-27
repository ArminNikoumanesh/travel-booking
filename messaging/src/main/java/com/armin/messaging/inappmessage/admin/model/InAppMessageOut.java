package com.armin.messaging.inappmessage.admin.model;

import com.armin.database.inappmessage.entity.InAppMessageEntity;
import com.armin.database.inappmessage.statics.constants.MessageSection;
import com.armin.database.user.model.UserInfo;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 20.04.24
 */
@Getter
@Setter
public class InAppMessageOut {
    private long id;
    private LocalDateTime expirationDate;
    private LocalDateTime createDate;
    private boolean isForMain;
    private boolean stayInBox;
    private String subject;
    private String messageText;
    private boolean inAppAction;
    private String url;
    private LocalDateTime lastUpdate;
    private MessageSection section;
    private Integer userId;
    private UserInfo userInfo;
    private boolean isRead;

    public InAppMessageOut(InAppMessageEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.expirationDate = entity.getExpirationDate();
            this.createDate = entity.getCreateDate();
            this.isForMain = entity.isForMain();
            this.stayInBox = entity.isStayInBox();
            this.subject = entity.getSubject();
            this.messageText = entity.getMessageText();
            this.inAppAction = entity.isInAppAction();
            this.url = entity.getUrl();
            this.lastUpdate = entity.getLastUpdate();
            this.section = entity.getSection();
            this.userId = entity.getUserId();
            this.isRead = entity.isRead();
            if (Hibernate.isInitialized(entity.getUser()) && entity.getUser() != null) {
                this.userInfo = new UserInfo(entity.getUser());
            }
        }
    }
}
