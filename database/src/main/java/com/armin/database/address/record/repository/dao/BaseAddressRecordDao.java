package com.armin.database.address.record.repository.dao;

import com.armin.database.address.record.entity.AddressRecordEntity;
import com.armin.utility.repository.orm.Dao;
import org.springframework.stereotype.Repository;

import jakarta.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Repository
public class BaseAddressRecordDao extends Dao<AddressRecordEntity> {

    public Optional<AddressRecordEntity> getUserAddressRecord(int userId, int id) {
        Query query = this.getEntityManager().createQuery(
                "SELECT record FROM AddressRecordEntity  record " +
                        "WHERE record.id = :id AND record.userId = :userId");
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("userId", userId);
        List<AddressRecordEntity> result = super.queryHql(query, map);
        return result.isEmpty() ? Optional.empty() : Optional.ofNullable(result.get(0));
    }
}
