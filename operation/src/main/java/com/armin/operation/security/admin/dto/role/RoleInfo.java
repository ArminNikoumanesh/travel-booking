package com.armin.operation.security.admin.dto.role;

import com.armin.database.user.entity.SecurityRoleEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author imax on 1/15/20
 */
@Getter
@Setter
public class RoleInfo {
    private Integer id;
    private String name;

    public RoleInfo(SecurityRoleEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.name = entity.getName();
        }
    }
}
