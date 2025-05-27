package com.armin.database.address.record.repository.service;

import com.armin.database.address.record.entity.AddressRecordEntity;
import com.armin.database.address.record.repository.dao.BaseAddressRecordDao;
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
public class BaseAddressRecordService extends AbstractService<AddressRecordEntity, BaseAddressRecordDao> {

    @Autowired
    public BaseAddressRecordService(BaseAddressRecordDao dao) {
        super(dao);
    }

    public AddressRecordEntity getUserAddressRecord(int userId, int id) throws SystemException {
        return getDao().getUserAddressRecord(userId, id).orElseThrow(
                () -> new SystemException(SystemError.DATA_NOT_FOUND, "address not found", 7715));
    }
}
