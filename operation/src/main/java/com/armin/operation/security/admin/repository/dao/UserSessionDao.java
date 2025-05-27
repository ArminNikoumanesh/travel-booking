package com.armin.operation.security.admin.repository.dao;

import com.armin.security.statics.constants.ClientType;
import com.armin.utility.repository.orm.Dao;
import com.armin.database.user.entity.UserSessionEntity;
import org.springframework.stereotype.Repository;

import jakarta.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author unascribed
 * @author Mohammad Yasin Sadeghi
 */
@Repository
public class UserSessionDao extends Dao<UserSessionEntity> {

    public UserSessionEntity getByUniqueId(Integer userId, String uniqueId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", userId);
        parameters.put("uniqueId", uniqueId);
        return super.getByAndConditions(parameters);
    }

    public void deleteByUserId(int userId) {
        Query query = this.getEntityManager().createQuery("delete from UserSessionEntity session where session.userId=:userId");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", userId);
        updateHqlQuery(query, parameters);
    }

    public List<String> getFirebaseTokenByUserIdsAndClientTypes(List<Integer> userIds, List<ClientType> clientTypes) {
        Query query = this.getEntityManager().createQuery("select session.firebaseToken from UserSessionEntity session where session.userId in (:userIds) and session.clientType in (:clientTypes) and session.firebaseToken is not null");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userIds", userIds);
        parameters.put("clientTypes", clientTypes);
        return queryHql(query, parameters);
    }

}
