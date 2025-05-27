package com.armin.operation.security.admin.repository.dao;

import com.armin.utility.repository.orm.Dao;
import com.armin.database.user.entity.SecurityRoleEntity;
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
public class RoleDao extends Dao<SecurityRoleEntity> {

    public void update(SecurityRoleEntity roleEntity) {
        super.update(roleEntity);
    }

    public void delete(Integer id) {
        super.deleteById(id);
    }

    /* ****************************************************************************************************************** */

    public SecurityRoleEntity getRole(Integer id) {
        return super.byId(id);
    }

    public List<SecurityRoleEntity> listByCategory(Integer category) {
        Map<String, Object> map = new HashMap<>();
        map.put("category", category);
        return super.listByAndConditions(map);
    }


    public SecurityRoleEntity getWithPermissions(Integer id) {
        Query query = this.getEntityManager().createQuery("SELECT role FROM SecurityRoleEntity role LEFT JOIN FETCH role.permissions WHERE role.id = :id");
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        List<SecurityRoleEntity> result = super.queryHql(query, map);
        return result.isEmpty() ? null : result.get(0);
    }

}
