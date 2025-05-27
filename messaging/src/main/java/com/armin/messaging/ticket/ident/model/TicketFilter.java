package com.armin.messaging.ticket.ident.model;


import com.armin.utility.repository.orm.service.FilterBase;
import com.armin.database.ticket.statics.TicketStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class TicketFilter implements FilterBase {
    private Integer id;
    private String subject;
    private TicketStatus status;
    private LocalDateTime createdMax;
    private LocalDateTime createdMin;
    @Setter(AccessLevel.PRIVATE)
//    @ApiModelProperty(hidden = true)
    private Integer userId;

    public void putUserId(Integer userId) {
        this.userId = userId;
    }
}
