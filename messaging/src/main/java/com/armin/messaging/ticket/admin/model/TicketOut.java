package com.armin.messaging.ticket.admin.model;

import com.armin.database.ticket.entity.TicketEntity;
import com.armin.database.user.model.UserInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
public class TicketOut extends TicketIn {
    private int id;
    private LocalDateTime created;
    private UserInfo user;
    @Setter(AccessLevel.PRIVATE)
    private List<TicketMessageOut> messages;

    public TicketOut(TicketEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.created = entity.getCreated();
            this.setPriority(entity.getPriority());
            this.setSubject(entity.getSubject());
            this.setStatus(entity.getStatus());
            this.setUserId(entity.getUserId());
            if (Hibernate.isInitialized(entity.getUser()) && entity.getUser() != null) {
                this.user = new UserInfo(entity.getUser());
            }
            if (Hibernate.isInitialized(entity.getUser()) && entity.getUser() != null) {
                this.user = new UserInfo(entity.getUser());
            }
            this.messages = new ArrayList<>();
            if (Hibernate.isInitialized(entity.getMessages())) {
                this.messages = entity.getMessages().stream().map(TicketMessageOut::new)
                        .sorted((m1, m2) -> m1.getCreated().compareTo(m2.getCreated()))
                        .collect(Collectors.toList());
            }



        }
    }
}
