package com.armin.utility.repository.odm.logs.repository;


import com.armin.utility.repository.odm.logs.entity.LogEntity;
import com.armin.utility.repository.orm.Dao;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogService extends Dao<LogEntity> {

    public void create(String logData,String cause) {
        LogEntity logEntity = new LogEntity();
        logEntity.setLogData(logData);
        logEntity.setCause(cause);
        logEntity.setCreated(LocalDateTime.now());
        saveEntity(logEntity);
    }

    public void create(List<String> logStrings) {
        for (String logString : logStrings) {
            LogEntity logEntity = new LogEntity();
            logEntity.setLogData(logString);
            saveEntity(logEntity);
        }
    }
}
