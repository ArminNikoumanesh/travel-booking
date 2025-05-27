package com.armin.database.global.repository.service;

import com.armin.database.global.entity.GlobalPropertyEntity;
import com.armin.database.global.model.GlobalPropertyEditeIn;
import com.armin.database.global.repository.dao.BaseGlobalPropertyDao;
import com.armin.utility.object.SystemException;
import com.armin.utility.repository.orm.AbstractService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Service
public class BaseGlobalPropertyService extends AbstractService<GlobalPropertyEntity, BaseGlobalPropertyDao> {
    private final ModelMapper modelMapper;

    @Autowired
    public BaseGlobalPropertyService(BaseGlobalPropertyDao dao, ModelMapper modelMapper) {
        super(dao);
        this.modelMapper = modelMapper;
    }

    public List<GlobalPropertyEntity> getAllEntities() {
        return getDao().getAllEntities();
    }

    public GlobalPropertyEntity update(GlobalPropertyEditeIn editeIn, String name, String profile) throws SystemException {
        GlobalPropertyEntity globalProperty = getById(name, profile);
        modelMapper.map(editeIn, globalProperty);
        return updateEntity(globalProperty);
    }

    public GlobalPropertyEntity getById(String name, String profile) {
        return getDao().getEntityById(name, profile);
    }
}
