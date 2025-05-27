package com.armin.operation.security.admin.dto.realm;

import com.armin.database.user.entity.SecurityRealmEntity;

/**
 * @author imax on 1/7/20
 */
public class RealmInfo {
    private Integer id;
    private String name;

    public RealmInfo(SecurityRealmEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.name = entity.getName();
        }
    }
}
