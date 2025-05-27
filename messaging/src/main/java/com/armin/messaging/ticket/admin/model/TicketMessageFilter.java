package com.armin.messaging.ticket.admin.model;

import com.armin.utility.repository.orm.service.FilterBase;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class TicketMessageFilter implements FilterBase {
    private Integer id;
    private Integer ticketId;
    private LocalDateTime createdMax;
    private LocalDateTime createdMin;
    private Integer userId;
    private String body;


}
