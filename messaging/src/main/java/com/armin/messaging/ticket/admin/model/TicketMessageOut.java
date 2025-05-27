package com.armin.messaging.ticket.admin.model;

import com.armin.utility.config.factory.StaticApplicationContext;
import com.armin.database.ticket.entity.TicketMessageEntity;
import com.armin.database.user.model.UserInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Setter
@Getter
public class TicketMessageOut extends TicketMessageIn {
    private Integer id;
    @Setter(AccessLevel.PRIVATE)
    private UserInfo user;
    @Setter(AccessLevel.PRIVATE)
    private UserInfo operator;
    private LocalDateTime created;
    private LocalDateTime seen;

    public TicketMessageOut(TicketMessageEntity entity) {
        ModelMapper modelMapper = StaticApplicationContext.getContext().getBean(ModelMapper.class);
        modelMapper.map(entity, this);
        super.putIp(entity.getIp());
        if (Hibernate.isInitialized(entity.getUser()) && entity.getUser()!=null) {
            this.user = new UserInfo(entity.getUser());
        }
        if (Hibernate.isInitialized(entity.getOperator()) && entity.getOperator()!=null) {
            this.operator = new UserInfo(entity.getOperator());
        }
    }
}
