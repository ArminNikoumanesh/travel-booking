package com.armin.messaging.inappmessage.ident.model;

import com.armin.database.inappmessage.statics.constants.MessageSection;
import com.armin.utility.repository.orm.service.FilterBase;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 20.04.24
 */
@Getter
@Setter
public class InAppMessageFilter implements FilterBase {
    private Long id;
    private LocalDateTime expirationDateMax;
    private LocalDateTime expirationDateMin;
    private LocalDateTime createDateMax;
    private LocalDateTime createDateMin;
    private Boolean isForMain;
    private Boolean stayInBox;
    private String subject;
    private String messageText;
    private Boolean inAppAction;
    private String url;
    private LocalDateTime lastUpdateMin;
    private LocalDateTime lastUpdateMax;
    private MessageSection section;
    private Boolean isRead;
    @Setter(AccessLevel.NONE)
    private Integer userId;

    public void putUserId(Integer userId) {
        this.userId = userId;
    }
}
