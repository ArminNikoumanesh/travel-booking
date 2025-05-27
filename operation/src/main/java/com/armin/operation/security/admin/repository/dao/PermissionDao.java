package com.armin.operation.security.admin.repository.dao;

import com.armin.utility.repository.orm.Dao;
import com.armin.database.user.entity.SecurityPermissionEntity;
import com.armin.database.user.statics.PermissionType;
import org.springframework.stereotype.Repository;

import jakarta.persistence.Query;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author unascribed
 * @author Mohammad Yasin Sadeghi
 */
@Repository
public class PermissionDao extends Dao<SecurityPermissionEntity> {

    public void update(SecurityPermissionEntity permissionEntity) {
        super.update(permissionEntity);
    }


    public SecurityPermissionEntity get(Integer id) {
        return super.byId(id);
    }

    public List<SecurityPermissionEntity> listLeafByIds(List<Integer> ids) {
        Query query = this.getEntityManager().createQuery("SELECT permission FROM SecurityPermissionEntity permission"
                + " WHERE permission.nodeType != 1 AND id IN (:ids)");
        Map<String, Object> map = new HashMap<>();
        map.put("ids", ids);
        return super.queryHql(query, map);
    }

    public Collection<SecurityPermissionEntity> listByIds(List<Integer> ids) {
        return super.listByIds(ids);
    }
}
