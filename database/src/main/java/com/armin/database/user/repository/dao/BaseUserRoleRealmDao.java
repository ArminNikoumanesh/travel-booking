package com.armin.database.user.repository.dao;

import com.armin.utility.repository.orm.Dao;
import com.armin.database.user.entity.SecurityUserRoleRealmEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Repository
public class BaseUserRoleRealmDao extends Dao<SecurityUserRoleRealmEntity> {

    public SecurityUserRoleRealmEntity getMemberRoleRealmByUserId(int userId, int roleId, int realmId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", userId);
        parameters.put("roleId", roleId);
        parameters.put("realmId", realmId);
        return super.getByAndConditions(parameters);
    }
}
