package com.armin.database.user.repository.service;

import com.armin.security.statics.constants.ClientType;
import com.armin.utility.repository.orm.AbstractService;
import com.armin.database.user.entity.UserSessionEntity;
import com.armin.database.user.repository.dao.BaseUserSessionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Service
public class BaseUserSessionService extends AbstractService<UserSessionEntity, BaseUserSessionDao> {

    @Autowired
    public BaseUserSessionService(BaseUserSessionDao dao) {
        super(dao);
    }

    public List<String> getFirebaseTokenByUserIdsAndClientTypes(List<Integer> userIds, List<ClientType> clientTypes) {
        return this.getDao().getFirebaseTokenByUserIdsAndClientTypes(userIds, clientTypes);
    }
}
