package com.armin.security.repository;

import com.armin.utility.object.UserContextDto;
import com.armin.security.model.object.SecurityAccessRule;
import com.armin.utility.repository.orm.Dao;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;


/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Repository
public class RestBaseDao extends Dao<UserContextDto> {

    public List<SecurityAccessRule> listRestsWithPermissions() {
        String sql =
                "select security_rest.http_method as \"httpMethod\"," +
                        " security_rest.url," +
                        " array_to_string(array_agg(distinct security_permission_rest.permission_id_fk), ',') as access" +
                        " from project.security_rest security_rest" +
                        " left join project.security_permission_rest security_permission_rest" +
                        " on security_rest.id_pk = security_permission_rest.rest_id_fk" +
                        " group by security_rest.id_pk";
        Query query = getSession().createNativeQuery(sql);
        List<SecurityAccessRule> result = querySql(query, new HashMap<>(), SecurityAccessRule.class);
        return result.isEmpty() ? null : result;
    }
}