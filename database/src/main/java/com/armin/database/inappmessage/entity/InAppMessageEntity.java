package com.armin.database.inappmessage.entity;

import com.armin.database.inappmessage.statics.constants.MessageSection;
import com.armin.database.user.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Entity
@Table(name = "IN_APP_MESSAGE")
@Getter
@Setter
public class InAppMessageEntity {
    @Id
    @Column(name = "ID_PK", scale = 0, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "In_App_Message_Sequence")
    @SequenceGenerator(name = "In_App_Message_Sequence", sequenceName = "IN_APP_MESSAGE_SEQ", allocationSize = 1)
    private long id;

    @Column(name = "EXPIRATION_DATE")
    private LocalDateTime expirationDate;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @Column(name = "IS_FOR_MAIN", columnDefinition = "bool default false")
    private boolean isForMain;

    @Column(name = "STAY_IN_BOX", columnDefinition = "bool default false")
    private boolean stayInBox;

    @Column(name = "IS_READ", columnDefinition = "bool default false")
    private boolean isRead;

    @Column(name = "SUBJECT", nullable = false)
    private String subject;

    @Column(name = "MESSAGE_TEXT", nullable = false)
    private String messageText;

    @Column(name = "IN_APP_ACTION", columnDefinition = "bool default true")
    private boolean inAppAction;

    @Column(name = "URL")
    private String url;

    @Column(name = "LAST_UPDATE", nullable = false)
    @UpdateTimestamp
    private LocalDateTime lastUpdate;

    @Enumerated(EnumType.STRING)
    @Column(name = "SECTION", nullable = false, length = 20)
    private MessageSection section;

    @Column(name = "USER_ID_FK")
    private int userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID_FK", insertable = false, updatable = false)
    private UserEntity user;
}
