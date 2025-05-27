package com.armin.messaging.ticket.admin.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Size;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Getter
@Setter
public class TicketMessageEditIn {
    @Size(max = 1000)
    private String body;
    @Size(max = 2000)
    private String attachments;
}
