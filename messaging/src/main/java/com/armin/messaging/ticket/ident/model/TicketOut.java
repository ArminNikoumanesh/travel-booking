package com.armin.messaging.ticket.ident.model;

import com.armin.utility.config.factory.StaticApplicationContext;
import com.armin.database.ticket.entity.TicketEntity;
import com.armin.database.ticket.statics.TicketPriority;
import com.armin.database.ticket.statics.TicketStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class TicketOut extends TicketIn {
    private Integer id;
    private LocalDateTime created;
    private TicketStatus status;
    @Setter(AccessLevel.PRIVATE)
    private List<TicketMessageOut> messages;
    private TicketPriority priority;

    public TicketOut(TicketEntity entity) {
        if (entity != null) {
            ModelMapper modelMapper = StaticApplicationContext.getContext().getBean(ModelMapper.class);
            modelMapper.map(entity, this);
            messages = new ArrayList<>();
            if (Hibernate.isInitialized(entity.getMessages())) {
                this.messages = entity.getMessages().stream().map(TicketMessageOut::new)
                        .sorted((m1, m2) -> m1.getCreated().compareTo(m2.getCreated()))
                        .collect(Collectors.toList());
            }
        }
    }
}
