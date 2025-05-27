package com.armin.operation.admin.model.dto.common;

import com.armin.database.user.entity.SecurityUserRoleRealmEntity;
import com.armin.database.user.entity.UserEntity;
import com.armin.operation.security.admin.dto.role.RoleOut;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class UserRoleOut {
    private int id;
    private String firstName;
    private String lastName;
    private Map<String, List<RoleOut>> realms = new HashMap<>();

    public UserRoleOut(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.firstName = userEntity.getFirstName();
        this.lastName = userEntity.getLastName();
        if (Hibernate.isInitialized(userEntity.getRoleRealms())) {
            Map<String, List<RoleOut>> roleRealms = new HashMap<>();
            for (SecurityUserRoleRealmEntity model : userEntity.getRoleRealms()) {
                if (Hibernate.isInitialized(model.getRole()) ) {
                    if (roleRealms.containsKey(String.valueOf(model.getRealmId()))) {
                        roleRealms.get(String.valueOf(model.getRealmId())).add(new RoleOut(model.getRole()));
                    } else {
                        List<RoleOut> roles = new ArrayList<>();
                        roles.add(new RoleOut(model.getRole()));
                        roleRealms.put(String.valueOf(model.getRealmId()), roles);
                    }
                }
            }
            this.realms = roleRealms;
        }
    }
}
