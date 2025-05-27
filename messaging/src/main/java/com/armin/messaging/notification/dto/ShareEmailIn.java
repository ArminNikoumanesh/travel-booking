package com.armin.messaging.notification.dto;

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
public class ShareEmailIn {
    @NotNull
    private String email;
    @NotNull
    private String sender;
    @NotNull
    private String url;
    @NotNull
    private String subject;
}
