package com.armin.messaging.ticket.admin.model;


import com.armin.database.ticket.entity.TicketMessageEntity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TicketMessageInfo {
    private Integer id;
    private String body;

    public TicketMessageInfo(TicketMessageEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.body = entity.getBody();
        }
    }
}
