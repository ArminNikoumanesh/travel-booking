package com.armin.operation.admin.model.dto.user;

import com.armin.database.user.entity.SecurityUserRoleRealmEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class RoleRealmIn {
    private Integer roleId;
    private Integer realmId;

    public RoleRealmIn(SecurityUserRoleRealmEntity entity) {
        if (entity != null) {
            this.roleId = entity.getRoleId();
            this.realmId = entity.getRealmId();
        }
    }

    @Override
    public String toString() {
        return "{" +
                "\"roleId\":" + roleId +
                ", \"realmId\":" + realmId +
                "}";
    }
}
