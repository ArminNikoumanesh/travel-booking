package com.armin.database.inappmessage.repository.dao;

import com.armin.database.inappmessage.entity.InAppMessageEntity;
import com.armin.utility.repository.orm.Dao;
import org.springframework.stereotype.Repository;

import jakarta.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Repository
public class BaseInAppMessageDao extends Dao<InAppMessageEntity> {

    public void updateAsRead(int userId, List<Long> ids) {
        Query query = this.getEntityManager().createQuery(
                "UPDATE InAppMessageEntity appMessage " +
                "SET appMessage.isRead = true  " +
                "WHERE appMessage.userId = :userId AND appMessage.id IN :ids");
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("ids", ids);
        super.updateHqlQuery(query, params);
    }
}
