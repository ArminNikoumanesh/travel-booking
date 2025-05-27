package com.armin.messaging.ticket.admin.model;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Setter
@Getter
public class TicketMessageIn {
    @Size(max = 1000)
    @NotBlank
    private String body;
    @Size(max = 2000)
    private String attachments;
//    @ApiModelProperty(hidden = true)
    @Setter(AccessLevel.PRIVATE)
    private String ip;

    public void putIp(String ip) {
        this.ip = ip;
    }

}
