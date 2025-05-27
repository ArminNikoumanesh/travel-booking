package com.armin.database.inappmessage.repository.service;

import com.armin.database.inappmessage.entity.InAppMessageEntity;
import com.armin.database.inappmessage.repository.dao.BaseInAppMessageDao;
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
public class BaseInAppMessageService extends AbstractService<InAppMessageEntity, BaseInAppMessageDao> {

    @Autowired
    public BaseInAppMessageService(BaseInAppMessageDao dao) {
        super(dao);
    }

    public void updateAsRead(int userId, List<Long> ids) {
        getDao().updateAsRead(userId,ids);
    }

    public void createBusinessMessage (InAppMessageEntity InAppMessageEntity){
        createEntity(InAppMessageEntity);
    }
}
