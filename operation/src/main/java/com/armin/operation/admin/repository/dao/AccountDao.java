package com.armin.operation.admin.repository.dao;

import com.armin.database.address.address.entity.UserAddressEntity;
import com.armin.utility.repository.orm.Dao;
import com.armin.operation.security.admin.repository.dao.UserDao;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther : Armin.Nik
 * @date : 31.08.22
 */
@Repository
public class AccountDao extends Dao<UserDao> {

    public void deleteUserSession(Integer userId) {
        String sql = "DELETE FROM project.user_session us  " +
                "WHERE us.user_id_fk = :userId ";
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        super.updateSql(sql, map);
    }

    public void deleteUserProfile(Integer userId) {
        String sql = "DELETE FROM project.profile p  " +
                "WHERE p.id_pk = :userId ";
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        super.updateSql(sql, map);
    }

    public void deleteUser(Integer userId) {
        String sql = "DELETE FROM project.users u  " +
                "WHERE u.id_pk = :userId ";
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        super.updateSql(sql, map);
    }

    public boolean existNationalId(String nationalId) {
        Query query = this.getEntityManager().createQuery(
                "SELECT user.nationalId " +
                        "FROM UserEntity user " +
                        "WHERE user.deleted IS NULL AND user.nationalId = :nationalId ");
        Map<String, Object> params = new HashMap<>();
        params.put("nationalId", nationalId);
        List<UserAddressEntity> result = super.queryHql(query, params);
        return !result.isEmpty();
    }

    public boolean existNationalId(int userId, String nationalId) {
        Query query = this.getEntityManager().createQuery(
                "SELECT user.nationalId " +
                        "FROM UserEntity user " +
                        "WHERE user.deleted IS NULL " +
                        "AND user.id != :userId " +
                        "AND user.nationalId = :nationalId " +
                        "AND user.verified IS TRUE ");
        Map<String, Object> params = new HashMap<>();
        params.put("nationalId", nationalId);
        params.put("userId", userId);
        List<UserAddressEntity> result = super.queryHql(query, params);
        return !result.isEmpty();
    }

}
