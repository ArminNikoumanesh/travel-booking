package com.armin.messaging.ticket.admin.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Getter
@Setter
@NoArgsConstructor
public class TicketTemplate {
    private String userName;
    private Integer ticketId;

    public TicketTemplate(String userName, Integer ticketId) {
        this.userName = userName;
        this.ticketId = ticketId;
    }
}
