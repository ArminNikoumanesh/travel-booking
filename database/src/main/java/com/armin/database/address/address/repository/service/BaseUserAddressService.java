package com.armin.database.address.address.repository.service;

import com.armin.database.address.address.entity.UserAddressEntity;

import com.armin.database.address.address.repository.dao.BaseUserAddressDao;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.utility.repository.orm.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Service
public class BaseUserAddressService extends AbstractService<UserAddressEntity, BaseUserAddressDao> {

    @Autowired
    public BaseUserAddressService(BaseUserAddressDao dao) {
        super(dao);
    }

    public void updateFalseDefaultForAll(int userId) {
        getDao().updateFalseDefaultForAll(userId);
    }

    public void setDefault(int userId, int id) {
        getDao().setDefault(userId, id);
    }

    public List<UserAddressEntity> getAll(int userId) {
        return getDao().getAll(userId);
    }

    public boolean checkUserDefaultAddress(int userId) {
        return getDao().checkUserDefaultAddress(userId);
    }

    public UserAddressEntity getByIdAndUserId(int userId, int id) throws SystemException {
        UserAddressEntity address = getDao().getByIdAndUserId(userId, id);
        if (address == null) {
            throw new SystemException(SystemError.DATA_NOT_FOUND, "id:" + id, 1201);
        } else {
            return address;
        }
    }
}
