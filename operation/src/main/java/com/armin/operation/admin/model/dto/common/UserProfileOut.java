package com.armin.operation.admin.model.dto.common;

import com.armin.utility.config.factory.StaticApplicationContext;
import com.armin.database.user.entity.SecurityUserRoleRealmEntity;
import com.armin.database.user.entity.UserEntity;
import com.armin.operation.admin.model.dto.profile.ProfileOut;
import com.armin.operation.admin.model.dto.user.UserOut;
import com.armin.operation.security.admin.dto.role.RoleOut;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class UserProfileOut {
    private UserOut user;
    private ProfileOut profile;

    public UserProfileOut() {
        this.user = new UserOut();
        this.profile = new ProfileOut();
    }

    public UserProfileOut(UserEntity userEntity) {
        this.user = new UserOut();
        this.profile = new ProfileOut();
        ModelMapper modelMapper = StaticApplicationContext.getContext().getBean(ModelMapper.class);
        modelMapper.map(userEntity, this.getUser());

        if (Hibernate.isInitialized(userEntity.getRoleRealms())) {
            Map<String, List<RoleOut>> roleRealms = new HashMap<>();
            for (SecurityUserRoleRealmEntity model : userEntity.getRoleRealms()) {
                if (Hibernate.isInitialized(model.getRole())) {
                    if (roleRealms.containsKey(String.valueOf(model.getRealmId()))) {
                        roleRealms.get(String.valueOf(model.getRealmId())).add(new RoleOut(model.getRole()));
                    } else {
                        List<RoleOut> roles = new ArrayList<>();
                        roles.add(new RoleOut(model.getRole()));
                        roleRealms.put(String.valueOf(model.getRealmId()), roles);
                    }
                }
            }
            this.getUser().setRealms(roleRealms);
        }

        if (Hibernate.isInitialized(userEntity.getProfile())) {
            this.profile = new ProfileOut(userEntity.getProfile());
        }
        this.user.setLegal(userEntity.isLegal());
    }
}
