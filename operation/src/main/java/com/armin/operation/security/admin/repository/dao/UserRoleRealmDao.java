package com.armin.operation.security.admin.repository.dao;

import com.armin.utility.repository.orm.Dao;
import com.armin.database.user.entity.SecurityUserRoleRealmEntity;
import org.springframework.stereotype.Repository;

import jakarta.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author imax on 5/20/19
 * @author Mohammad Yasin Sadeghi
 */

@Repository
public class UserRoleRealmDao extends Dao<SecurityUserRoleRealmEntity> {

    public SecurityUserRoleRealmEntity save(SecurityUserRoleRealmEntity ss) {
        return super.saveEntity(ss);
    }

    public void delete(int roleId, int realmId, int userId) {
        Query query = this.getEntityManager().createQuery("delete from SecurityUserRoleRealmEntity roleRealms " +
                "where roleRealms.roleId = :roleId and roleRealms.realmId = :realmId and roleRealms.userId = :userId");
        Map<String, Object> params = new HashMap<>();
        params.put("roleId", roleId);
        params.put("realmId", realmId);
        params.put("userId", userId);
        super.updateHqlQuery(query, params);
    }

    public List<SecurityUserRoleRealmEntity> getByRealmId(Integer realmId) {
        Map<String, Object> params = new HashMap<>();
        params.put("realmId", realmId);
        return super.listByAndConditions(params);
    }

    public void deleteByIdList(List<Integer> ids) {
        super.deleteByIdList(ids, "id");
    }
}
