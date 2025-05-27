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
public class UserSessionBaseDao extends Dao<UserContextDto> {

    public UserContextDto getSessionWithUser(int id) {
        String sql = "select user_session.id_pk as \"sessionId\"," +
                " users.id_pk as \"id\"," +
                " users.suspended," +
                " users.mobile_confirmed as \"mobileConfirmed\"," +
                " users.email_confirmed as \"emailConfirmed\"," +
                " users.full_name as \"fullName\"," +
                " array_to_string(array_agg(distinct security_role_permission.permission_id_fk), ',') as \"permissionIds\"" +
                " from project.user_session user_session " +
                " inner join project.users users " +
                " on user_session.user_id_fk = users.id_pk" +
                " left join project.security_user_role_realm user_role_realm" +
                " on users.id_pk = user_role_realm.user_id_fk" +
                " left join project.security_role_permission security_role_permission" +
                " on  user_role_realm.role_id_fk = security_role_permission.role_id_fk" +
                " where user_session.id_pk =:id and users.deleted is null"+
                " group by user_session.id_pk,users.id_pk";

        Query query = getSession().createNativeQuery(sql);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        List<UserContextDto> result = querySql(query, parameters, UserContextDto.class);
        return result.isEmpty() ? null : result.get(0);
    }
}
