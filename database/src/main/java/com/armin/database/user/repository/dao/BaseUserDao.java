package com.armin.database.user.repository.dao;


import com.armin.database.user.entity.UserEntity;
import com.armin.utility.repository.orm.Dao;
import org.springframework.stereotype.Repository;

import jakarta.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BaseUserDao extends Dao<UserEntity> {

    public UserEntity getByNationalId(String nationalId) {
        Query query = this.getEntityManager().createQuery("SELECT user FROM UserEntity user " +
                "where user.nationalId = :nationalId and user.mobileConfirmed = true AND user.deleted is null");
        Map<String, Object> map = new HashMap<>();
        map.put("nationalId", nationalId);
        List<UserEntity> result = super.queryHql(query, map);
        return result.isEmpty() ? null : result.get(0);
    }

    public boolean checkNationalId(String nationalId, int id){
        Query query = this.getEntityManager().createQuery("SELECT user.id FROM UserEntity user " +
                "WHERE user.nationalId = :nationalId AND user.id <> :id AND user.deleted IS NULL ");
        Map<String, Object> map = new HashMap<>();
        map.put("nationalId", nationalId);
        map.put("id", id);
        List<Integer> result = super.queryHql(query, map);
        return !result.isEmpty();
    }

    public boolean checkNationalId(String nationalId){
        Query query = this.getEntityManager().createQuery("SELECT user.id FROM UserEntity user " +
                "WHERE user.nationalId = :nationalId AND user.deleted IS NULL ");
        Map<String, Object> map = new HashMap<>();
        map.put("nationalId", nationalId);
        List<Integer> result = super.queryHql(query, map);
        return !result.isEmpty();
    }
}
