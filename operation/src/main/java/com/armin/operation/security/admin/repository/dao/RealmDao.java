package com.armin.operation.security.admin.repository.dao;

import com.armin.utility.repository.orm.Dao;
import com.armin.database.user.entity.SecurityRealmEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author unascribed
 * @author Mohammad Yasin Sadeghi
 */
@Repository
public class RealmDao extends Dao<SecurityRealmEntity> {

    public void update(SecurityRealmEntity realmEntity) {
        super.update(realmEntity);
    }

    public SecurityRealmEntity get(Integer id) {
        return super.byId(id);
    }

    public void deletePersonRoleRealmByRealmId(Integer realmId) {
        String sql = "delete from SECURITY_PERSON_ROLE_REALM where REALM_ID_FK = :realmId";
        Map<String, Object> map = new HashMap<>();
        map.put("realmId", realmId);
        super.querySql(sql, map);
    }

    public List<SecurityRealmEntity> getEntitiesByIds(List<Integer> ids) {
        return super.listByIds(ids);
    }

}
