package com.armin.operation.security.admin.repository.dao;

import com.armin.utility.repository.orm.Dao;
import com.armin.database.user.entity.UserEntity;
import org.springframework.stereotype.Repository;

import jakarta.persistence.Query;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author unascribed
 * @author Mohammad Yasin Sadeghi
 */
@Repository
public class UserDao extends Dao<UserEntity> {

    public UserEntity get(Integer id, String[] include) {
        Query query = this.getEntityManager().createQuery("select users from UserEntity users where users.id = :id and users.deleted is null");
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        List<UserEntity> result = super.queryHql(query, map, UserEntity.class, include);
        return !result.isEmpty() ? result.get(0) : null;
    }

    /**
     * I prepared this method for following use case:
     * In OrderStatus module, the specific user edit and save the specific order status and after a while, he/she
     * deleted from App User Table logically. In this scenario, we need to have query him/her first name and last name,
     * Therefore, with following query we don't consider logical deletion.
     *
     * @param id: user ID
     * @return user Entity
     * Alireza Alimohammadi
     * 2019-02-24
     */
    public UserEntity getWithoutConsiderationLogicalDeletion(Integer id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        return super.getByAndConditions(map);
    }

    public List<UserEntity> list() {
        Query query = this.getEntityManager().createQuery("SELECT user FROM UserEntity user WHERE user.deleted is null");
        Map<String, Object> map = new HashMap<>();
        return super.queryHql(query, map);
    }

    /* ****************************************************************************************************************** */

    public UserEntity getWithRole(Integer userId) {
        Query query = this.getEntityManager().createQuery("SELECT user FROM UserEntity user LEFT JOIN FETCH user.roleRealms roleRealms WHERE user.id = :userId AND user.deleted is null");
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        List<UserEntity> result = super.queryHql(query, map);
        return result.isEmpty() ? null : result.get(0);
    }

    /* ****************************************************************************************************************** */

    public UserEntity getByAuthentication(Integer userId) {
        Query query = this.getEntityManager().createQuery("SELECT user FROM UserEntity user " +
                "LEFT JOIN FETCH user.roleRealms roleRealms " +
                "LEFT JOIN FETCH roleRealms.role role " +
                "LEFT JOIN FETCH role.permissions " +
                "WHERE user.id = :userId AND user.deleted is null");
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        List<UserEntity> result = super.queryHql(query, map);
        return result.isEmpty() ? null : result.get(0);
    }

    /* ****************************************************************************************************************** */

    public boolean existReferrerIdByCode(int code) {
        Query query = this.getEntityManager().createQuery("SELECT profile.id FROM ProfileEntity profile " +
                "where profile.code = :code");

        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        List<Integer> result = super.queryHql(query, map);
        return !result.isEmpty();
    }

    public UserEntity getReferrer(Integer code, String mobile) {
        Query query = this.getEntityManager().createQuery("SELECT user FROM UserEntity user " +
                "inner join fetch user.profile profile " +
                "where (profile.code = :code OR user.mobile = :mobile) AND user.deleted is null");

        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("mobile", mobile);
        List<UserEntity> result = super.queryHql(query, map);
        return result.isEmpty() ? null : result.get(0);
    }

    public UserEntity getByUsername(String username) {
        Query query = this.getEntityManager().createQuery("SELECT user FROM UserEntity user " +
                "left join fetch user.roleRealms roleRealms " +
                "left join fetch roleRealms.role role " +
                "left join fetch role.permissions " +
                "join fetch user.profile " +
                "where (user.mobile = :username OR user.email = :username) AND user.deleted is null");

        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        List<UserEntity> result = super.queryHql(query, map);
        return result.isEmpty() ? null : result.get(0);
    }

    public UserEntity getByDeviceId(String deviceId) {
        Query query = this.getEntityManager().createQuery("SELECT user FROM UserEntity user " +
            "left join fetch user.roleRealms roleRealms " +
            "left join fetch roleRealms.role role " +
            "left join fetch role.permissions " +
            "join fetch user.profile " +
            "where user.deviceId = :deviceId AND user.deleted is null");

        Map<String, Object> map = new HashMap<>();
        map.put("deviceId", deviceId);
        List<UserEntity> result = super.queryHql(query, map);
        return result.isEmpty() ? null : result.get(0);
    }

    public UserEntity getByUsername(String mobile, String email) {
        Query query = this.getEntityManager().createQuery("SELECT user FROM UserEntity user where (user.mobile = :mobile OR user.email = :email) AND user.deleted is null");
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("email", email);
        List<UserEntity> result = super.queryHql(query, map);
        return result.isEmpty() ? null : result.get(0);
    }

    public UserEntity getByUsername(String mobile, String email, int id) {
        Query query = this.getEntityManager().createQuery("SELECT user FROM UserEntity user" +
                " where (user.mobile = :mobile OR user.email = :email)" +
                " AND user.deleted is null" +
                " AND user.id <>:id");
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("email", email);
        map.put("id", id);
        List<UserEntity> result = super.queryHql(query, map);
        return result.isEmpty() ? null : result.get(0);
    }

    public UserEntity getByNationalId(String nationalId) {
        Query query = this.getEntityManager().createQuery("SELECT user FROM UserEntity user " +
                "where user.nationalId = :nationalId and user.mobileConfirmed = true AND user.deleted is null");
        Map<String, Object> map = new HashMap<>();
        map.put("nationalId", nationalId);
        List<UserEntity> result = super.queryHql(query, map);
        return result.isEmpty() ? null : result.get(0);
    }

    public UserEntity getByUsernameWithProfile(String mobile, String email) {
        Query query = this.getEntityManager().createQuery("SELECT user FROM UserEntity user " +
                " left join fetch user.profile " +
                " left join fetch user.roleRealms roleRealm " +
                " left join fetch roleRealm.role role " +
                " left join fetch role.permissions " +
                " where (user.mobile = :mobile OR user.email = :email) AND user.deleted is null");
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("email", email);
        List<UserEntity> result = super.queryHql(query, map);
        return result.isEmpty() ? null : result.get(0);
    }

    public void updateAccessFailedCount(int id) {
        Query query = this.getEntityManager().createQuery("update UserEntity user " +
                " set user.accessFailedCount = 0 " +
                " where user.id=:id");
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        super.updateHqlQuery(query, map);
    }

    public Boolean delete(Integer userId) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", userId);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("deleted", LocalDateTime.now());
        return super.updateByAndConditions(conditions, parameters);
    }

    public Boolean changeTwoFactorLogin(Integer userId, boolean status) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", userId);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("twoFactorEnabled", status);
        return super.updateByAndConditions(conditions, parameters);
    }

    public Boolean changeSuspend(Integer userId, boolean status) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", userId);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("suspended", status);
        return super.updateByAndConditions(conditions, parameters);
    }

    public void changeLockStatus(Integer userId, LocalDateTime lockExpired) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", userId);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("lockExpired", lockExpired);
        super.updateByAndConditions(conditions, parameters);
    }

    public UserEntity getByEmail(String email) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        return super.getByAndConditions(params);
    }

    public void confirmMobile(Integer userId, boolean mobileConfirmed) {
        Query query = this.getEntityManager().createQuery("update UserEntity user set user.mobileConfirmed=:mobileConfirmed where user.id=:userId");
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("mobileConfirmed", mobileConfirmed);
        updateHqlQuery(query, params);
    }

    public UserEntity getByUserId(int id) {
        Query query = this.getEntityManager().createQuery("SELECT user FROM UserEntity user " +
                "left join fetch user.roleRealms roleRealms " +
                "left join fetch roleRealms.role role " +
                "left join fetch role.permissions " +
                "where user.id = :id AND user.deleted is null");

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        List<UserEntity> result = super.queryHql(query, map);
        return result.isEmpty() ? null : result.get(0);
    }
}
