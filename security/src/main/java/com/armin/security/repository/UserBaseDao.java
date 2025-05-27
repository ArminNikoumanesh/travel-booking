package com.armin.security.repository;

import com.armin.utility.object.UserContextDto;
import com.armin.utility.repository.orm.Dao;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Repository
public class UserBaseDao extends Dao<UserContextDto> {

    public UserContextDto getByAuthentication(Integer userId) {
        String sql = "select users.id_pk as id," +
                " users.full_name as \"fullName\"," +
                " users.suspended," +
                " users.mobile_confirmed as \"mobileConfirmed\"," +
                " users.email_confirmed as \"emailConfirmed\"," +
                " array_to_string(array_agg(distinct security_role_permission.permission_id_fk), ',') as \"permissionIds\"" +
                " from project.users users" +
                " left join project.security_user_role_realm user_role_realm" +
                " on users.id_pk = user_role_realm.user_id_fk" +
                " left join project.security_role security_role" +
                " on user_role_realm.role_id_fk = security_role.id_pk" +
                " left join project.security_role_permission security_role_permission" +
                " on security_role.id_pk = security_role_permission.role_id_fk" +
                " where users.id_pk =:userId and users.deleted is null" +
                " group by users.id_pk";
        Query query = getSession().createNativeQuery(sql);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", userId);
        List<UserContextDto> result = querySql(query, parameters, UserContextDto.class);
        return result.isEmpty() ? null : result.get(0);
    }
}
