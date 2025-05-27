package com.armin.operation.security.admin.repository.dao;

import com.armin.utility.repository.orm.Dao;
import com.armin.database.user.entity.SecurityRestEntity;
import org.springframework.stereotype.Repository;


/**
 * @author unascribed
 * @author Mohammad Yasin Sadeghi
 */
@Repository
public class RestDao extends Dao<SecurityRestEntity> {

    public SecurityRestEntity get(Integer id) {
        return super.byId(id);
    }

}
