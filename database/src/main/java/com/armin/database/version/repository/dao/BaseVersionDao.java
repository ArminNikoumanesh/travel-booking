package com.armin.database.version.repository.dao;

import com.armin.database.version.entity.ServiceType;
import com.armin.database.version.entity.VersionEntity;
import com.armin.utility.repository.orm.Dao;
import org.springframework.stereotype.Repository;

import jakarta.persistence.Query;
import java.util.*;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Repository
public class BaseVersionDao extends Dao<VersionEntity> {

    public Optional<VersionEntity> getEntityByType(ServiceType type) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("type", type);
        VersionEntity result = super.getByAndConditions(parameters);
        return result == null ? Optional.empty() : Optional.of(result);
    }

    public List<VersionEntity> getAll() {
        Query query = this.getEntityManager().createQuery(
                "SELECT versions FROM VersionEntity versions ");
        Map<String, Object> parameters = new HashMap<>();
        List<VersionEntity> result = super.queryHql(query, parameters);
        return result == null ? new ArrayList<>(0) : result;
    }
}
