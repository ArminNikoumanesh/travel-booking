package com.armin.messaging.ticket.ident.repository.dao;


import com.armin.utility.repository.orm.Dao;
import com.armin.database.ticket.entity.TicketEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class IdentTicketDao extends Dao<TicketEntity> {

    public TicketEntity getByIdAndUserId(int userId, int id, String[] include) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", userId);
        parameters.put("id", id);
        List<TicketEntity> messages = listByAndConditions(parameters, include);
        return messages.isEmpty() ? null : messages.get(0);
    }
}
