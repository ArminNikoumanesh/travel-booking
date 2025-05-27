package com.armin.database.address.address.repository.dao;

import com.armin.database.address.address.entity.UserAddressEntity;
import com.armin.utility.repository.orm.Dao;
import org.springframework.stereotype.Repository;

import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Repository
public class BaseUserAddressDao extends Dao<UserAddressEntity> {

    public void updateFalseDefaultForAll(int userId) {
        Query query = this.getEntityManager().createQuery("UPDATE UserAddressEntity userAddress " +
                "SET userAddress.defaultAddress = false  " +
                "WHERE userAddress.userId = :userId ");
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        super.updateHqlQuery(query, params);
    }

    public void setDefault(int userId, int id) {
        Query query = this.getEntityManager().createQuery("UPDATE UserAddressEntity userAddress " +
                "SET userAddress.defaultAddress = true  " +
                "WHERE userAddress.userId = :userId AND userAddress.id = :id ");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("userId", userId);
        super.updateHqlQuery(query, params);
    }

    public List<UserAddressEntity> getAll(int userId) {
        Query query = this.getEntityManager().createQuery(
                "SELECT address " +
                        "FROM UserAddressEntity address " +
                        "LEFT JOIN FETCH address.addressRecord record " +
                        "WHERE address.userId = :userId AND address.deleted IS NULL ");
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        List<UserAddressEntity> result = super.queryHql(query, params);
        return result.isEmpty() ? new ArrayList<>(0) : result;
    }

    public boolean checkUserDefaultAddress(int userId) {
        Query query = this.getEntityManager().createQuery(
                "SELECT address " +
                        "FROM UserAddressEntity address " +
                        "WHERE address.deleted IS NULL AND address.userId = :userId ");
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        List<UserAddressEntity> result = super.queryHql(query, params);
        return result.isEmpty();
    }

    public UserAddressEntity getByIdAndUserId(int userId, int id) {
        Query query = this.getEntityManager().createQuery("SELECT address " +
                "FROM UserAddressEntity address " +
                "WHERE address.deleted IS NULL AND address.userId = :userId  AND address.id = :id");
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("id", id);
        List<UserAddressEntity> result = super.queryHql(query, params);
        return result.isEmpty() ? null : result.get(0);
    }
}