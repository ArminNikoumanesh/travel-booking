package com.armin.messaging.ticket.admin.model;


import com.armin.utility.object.IValidation;
import com.armin.utility.object.SystemException;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Setter
@Getter
public class TicketIn extends TicketEditIn implements IValidation {
    @NotNull
    private String subject;
    private Integer userId;
    @Valid
    @NotNull
    private TicketMessageIn message;


    @Override
    public void validate() throws SystemException {
    }
}
