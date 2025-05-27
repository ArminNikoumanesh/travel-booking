package com.armin.operation.security.admin.dto.permission;

import com.armin.database.user.entity.SecurityPermissionEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PermissionInfo {
    private int id;
    private String name;

    public PermissionInfo(SecurityPermissionEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.name = entity.getName();
        }
    }
}
