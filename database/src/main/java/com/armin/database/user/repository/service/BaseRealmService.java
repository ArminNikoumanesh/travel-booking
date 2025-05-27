package com.armin.database.user.repository.service;

import com.armin.utility.repository.orm.AbstractService;
import com.armin.utility.repository.orm.Dao;
import com.armin.database.user.entity.SecurityRealmEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Service
public class BaseRealmService extends AbstractService<SecurityRealmEntity, Dao<SecurityRealmEntity>> {

    @Autowired
    public BaseRealmService() {

    }
}
