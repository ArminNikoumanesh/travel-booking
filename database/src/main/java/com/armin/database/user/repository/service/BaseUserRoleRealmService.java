package com.armin.database.user.repository.service;

import com.armin.utility.repository.orm.AbstractService;
import com.armin.database.user.entity.SecurityUserRoleRealmEntity;
import com.armin.database.user.repository.dao.BaseUserRoleRealmDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Service
public class BaseUserRoleRealmService extends AbstractService<SecurityUserRoleRealmEntity, BaseUserRoleRealmDao> {

    @Autowired
    public BaseUserRoleRealmService(BaseUserRoleRealmDao dao) {
        super(dao);
    }

    public void createUserMemberRoleRealm(SecurityUserRoleRealmEntity entity) {
        SecurityUserRoleRealmEntity roleRealmEntity = getDao().getMemberRoleRealmByUserId(entity.getUserEntity().getId(), entity.getRoleId(), entity.getRealmId());
        if (roleRealmEntity == null) {
            this.createEntity(entity);
        }
    }

}
