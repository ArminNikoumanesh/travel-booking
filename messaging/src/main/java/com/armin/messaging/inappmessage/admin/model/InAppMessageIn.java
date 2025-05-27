package com.armin.messaging.inappmessage.admin.model;

import com.armin.database.inappmessage.statics.constants.MessageSection;
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
public class InAppMessageIn {
    private LocalDateTime expirationDate;
    private boolean isForMain;
    private boolean stayInBox;
    private String subject;
    private String messageText;
    private boolean inAppAction;
    private String url;
    private MessageSection section;
    private int userId;
}
