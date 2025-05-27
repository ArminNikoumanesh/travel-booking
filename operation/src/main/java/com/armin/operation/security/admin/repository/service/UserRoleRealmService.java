package com.armin.operation.security.admin.repository.service;

import com.armin.utility.repository.orm.AbstractService;
import com.armin.database.user.entity.SecurityUserRoleRealmEntity;
import com.armin.operation.security.admin.repository.dao.UserRoleRealmDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author imax on 5/20/19
 * @author Mohammad Yasin Sadeghi
 */

@Service
public class UserRoleRealmService extends AbstractService<SecurityUserRoleRealmEntity, UserRoleRealmDao> {

    @Autowired
    public UserRoleRealmService(UserRoleRealmDao userRoleRealmDao) {
        super(userRoleRealmDao);
    }

    public SecurityUserRoleRealmEntity save(SecurityUserRoleRealmEntity ss) {
        return getDao().save(ss);
    }

    public void delete(int roleId, int realmId, int userId) {
        getDao().delete(roleId, realmId, userId);
    }

    public void deleteByIdList(List<Integer> ids) {
        getDao().deleteByIdList(ids);
    }

    public List<SecurityUserRoleRealmEntity> getByRealmId(Integer realmId) {
        return getDao().getByRealmId(realmId);
    }

}
