package com.armin.database.pushnotification.entity;

import com.armin.utility.file.model.object.Attachment;
import com.armin.database.user.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Table(name = "push_notification_log")
@Entity
@Setter
@Getter
public class PushNotificationLogEntity {
    @Id
    @Column(name = "ID_PK", scale = 0, precision = 19)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Push_Notification_Log_Sequence")
    @SequenceGenerator(name = "Push_Notification_Log_Sequence", sequenceName = "PUSH_NOTIFICATION_LOG_SEQ", allocationSize = 10)
    private long id;
    @Basic
    @Column(name = "subject", length = 140, nullable = false)
    private String subject;
    @Basic
    @Column(name = "content", length = 1024, nullable = false)
    private String content;
    @Column(name = "USER_IDS", length = 160)
    private String userIds;
    @Column(name = "USER_LEVEL_IDS", length = 160)
    private String userLevelIds;
    @Column(name = "USER_GROUP_IDS", length = 160)
    private String userGroupIds;
    @Column(name = "data", length = 500)
    private String data;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "TOPIC", length = 50)
    private PushNotificationTopic topic;
    @Basic
    @Column(name = "IMAGE", length = 100)
    @Attachment(container = "push", bucket = "notification")
    private String image;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "NOTIFICATION_TYPE", nullable = false, length = 20)
    private PushNotificationType notificationType;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "SERVICE_TYPE", nullable = false, length = 20)
    private PushNotificationServiceType serviceType;
    @Column(name = "CLIENT_TYPES", length = 50)
    private String clientTypes;
    @Column(name = "OPERATOR_ID_FK", nullable = false, scale = 0, precision = 10)
    private int operatorId;
    @Column(name = "SENDER_ID_FK", scale = 0, precision = 10)
    private Integer senderId;
    @Column(name = "SILENT", nullable = false)
    private boolean silent;
    @Column(name = "TTL", scale = 0, precision = 19)
    private Long ttl;
    @Basic
    @Column(name = "ICON", length = 100)
    @Attachment(container = "push", bucket = "icon")
    private String icon;
    @Basic
    @Column(name = "CREATED", nullable = false)
    private LocalDateTime created;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OPERATOR_ID_FK", insertable = false, updatable = false)
    private UserEntity operator;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SENDER_ID_FK", insertable = false, updatable = false)
    private UserEntity sender;
    @Column(name = "SEND_DATE")
    private LocalDateTime sendDate;

    public PushNotificationLogEntity cloneImages() {
        PushNotificationLogEntity entity = new PushNotificationLogEntity();
        entity.setImage(this.image);
        entity.setIcon(this.icon);
        return entity;
    }


    @PrePersist
    public void prePersist() {
        this.created = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.created = LocalDateTime.now();
    }
}
