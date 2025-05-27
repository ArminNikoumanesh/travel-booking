package com.armin.messaging.ticket.ident.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Setter
@Getter
public class TicketIn {
    @NotNull
    private String subject;
    @Valid
    @NotNull
    private TicketMessageIn message;

}
