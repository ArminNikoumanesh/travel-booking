package com.armin.database.global.repository.dao;

import com.armin.database.global.entity.GlobalPropertyEntity;
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
public class BaseGlobalPropertyDao extends Dao<GlobalPropertyEntity> {

    public List<GlobalPropertyEntity> getAllEntities() {
        Query query = this.getEntityManager().createQuery(" SELECT property FROM GlobalPropertyEntity property ");
        Map<String, Object> map = new HashMap<>();
        List<GlobalPropertyEntity> result = super.queryHql(query, map);
        return result.isEmpty() ? new ArrayList<>(0) : result;
    }

    //this is composite id
    public GlobalPropertyEntity getEntityById(String name, String profile) {
        Query query = this.getEntityManager().createQuery(" SELECT property FROM GlobalPropertyEntity property " +
                "WHERE property.id.profile = :profile AND property.id.name = :name ");
        Map<String, Object> map = new HashMap<>();
        map.put("name",name);
        map.put("profile",profile);
        List<GlobalPropertyEntity> result = super.queryHql(query, map);
        return result.isEmpty() ? null : result.get(0);
    }

}
