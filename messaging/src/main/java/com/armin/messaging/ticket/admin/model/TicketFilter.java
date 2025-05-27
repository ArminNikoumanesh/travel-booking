package com.armin.messaging.ticket.admin.model;

import com.armin.utility.repository.orm.service.FilterBase;
import com.armin.database.ticket.statics.TicketPriority;
import com.armin.database.ticket.statics.TicketStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class TicketFilter implements FilterBase {
    private Integer id;
    private String subject;
    private TicketStatus status;
    private TicketPriority priority;
    private LocalDateTime createdMax;
    private LocalDateTime createdMin;
    private Integer userId;
    private Integer operatorId;

}
