package com.armin.messaging.template.repository.dao;

import com.armin.utility.repository.orm.Dao;
import com.armin.infrastructure.common.HandlebarsTemplateEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Repository
public class HandlebarsTemplateDao extends Dao<HandlebarsTemplateEntity> {
    public HandlebarsTemplateEntity getByName(String name) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        return this.getByAndConditions(parameters, null);
    }
}
