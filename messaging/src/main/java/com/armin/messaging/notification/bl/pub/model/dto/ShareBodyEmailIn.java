package com.armin.messaging.notification.bl.pub.model.dto;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Getter
@Setter
public class ShareBodyEmailIn {
    @NotNull
    private String email;
    private String sender;
    @NotNull
    private String body;
    private String subject;
}
