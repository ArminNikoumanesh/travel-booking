package com.armin.database.version.repository.service;

import com.armin.database.version.entity.ServiceType;
import com.armin.database.version.entity.VersionEntity;
import com.armin.database.version.repository.dao.BaseVersionDao;
import com.armin.utility.repository.orm.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Service
public class BaseVersionService extends AbstractService<VersionEntity, BaseVersionDao> {

    @Autowired
    public BaseVersionService(BaseVersionDao dao) {
        super(dao);
    }

    public VersionEntity getEntityByType(ServiceType type) {
        return getDao().getEntityByType(type).orElse(new VersionEntity(type));
    }

    public void createOrUpdateVersion(ServiceType type) {
        VersionEntity entity = getEntityByType(type);
        entity.setLastModified(LocalDateTime.now());
        createOrUpdateEntity(entity);
    }

    public List<VersionEntity> getAllEntities() {
        return getDao().getAll();
    }


}
