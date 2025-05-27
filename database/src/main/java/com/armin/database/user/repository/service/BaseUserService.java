package com.armin.database.user.repository.service;


import com.armin.database.user.entity.UserEntity;
import com.armin.database.user.repository.dao.BaseUserDao;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.utility.repository.orm.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Service
public class BaseUserService extends AbstractService<UserEntity, BaseUserDao> {

    @Autowired
    public BaseUserService(BaseUserDao dao) {
        super(dao);
    }

    public UserEntity getByNationalId(String nationalId) {
        return getDao().getByNationalId(nationalId);
    }

    public boolean checkNationalId(String nationalId, int id) throws SystemException {
        if (getDao().checkNationalId(nationalId, id)) {
            throw new SystemException(SystemError.DUPLICATE_REQUEST, "duplicate national id", 3678);
        }
        return true;
    }

    public boolean checkNationalId(String nationalId) throws SystemException {
        if (getDao().checkNationalId(nationalId)) {
            throw new SystemException(SystemError.DUPLICATE_REQUEST, "duplicate national id", 3678);
        }
        return true;
    }

}
