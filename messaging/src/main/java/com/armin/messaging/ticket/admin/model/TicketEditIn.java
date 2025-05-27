package com.armin.messaging.ticket.admin.model;


import com.armin.database.ticket.statics.TicketPriority;
import com.armin.database.ticket.statics.TicketStatus;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Setter
@Getter
public class TicketEditIn {
    @NotNull
    private TicketStatus status;
    @NotNull
    private TicketPriority priority;

}
