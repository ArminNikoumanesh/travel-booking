package com.armin.messaging.ticket.ident.repository.dao;

import com.armin.utility.repository.orm.Dao;
import com.armin.database.ticket.entity.TicketMessageEntity;
import org.springframework.stereotype.Repository;

import jakarta.persistence.Query;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Repository
public class IdentTicketMessageDao extends Dao<TicketMessageEntity> {

    public void seenTicketMessage(int ticketId) {
        Query query = this.getEntityManager().createQuery("update TicketMessageEntity set seen =: now " +
                " where ticketId =: ticketId " +
                " and operatorId is not null " +
                " and seen is null ");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("now", LocalDateTime.now());
        parameters.put("ticketId", ticketId);

        updateHqlQuery(query, parameters);
    }
}
