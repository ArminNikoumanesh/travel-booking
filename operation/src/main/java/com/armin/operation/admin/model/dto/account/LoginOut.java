package com.armin.operation.admin.model.dto.account;

import com.armin.security.statics.enums.RoleCategoryType;
import com.armin.utility.object.TokenInfo;
import com.armin.database.user.entity.SecurityPermissionEntity;
import com.armin.database.user.entity.SecurityUserRoleRealmEntity;
import com.armin.database.user.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class LoginOut {
    @NotNull
    private AccountInfoOut account;
    private String accessToken;
    private String refreshToken;
    private Long ttl;
    private Long refreshTtl;
    private LocalDateTime creationTime;
    private Set<String> permissions;
    private boolean superAdmin;

    public LoginOut() {
    }

    public LoginOut(AccountInfoOut account) {
        this.account = account;
        this.permissions = new HashSet<>();
    }

    public LoginOut(UserEntity userEntity, TokenInfo token) {
        this.account = new AccountInfoOut(userEntity);
        this.account.setEmail(userEntity.getEmail());
        this.account.setMobile(userEntity.getMobile());
        this.accessToken = token.getAccessToken();
        this.refreshToken = token.getRefreshToken();
        this.ttl = token.getTtl();
        this.refreshTtl = token.getRefreshTtl();
        this.creationTime = token.getCreationTime();
        this.permissions = new HashSet<>();
        if (Hibernate.isInitialized(userEntity.getProfile()) && userEntity.getProfile() != null) {
            this.account.setBirthDate(userEntity.getProfile().getBirthDate());
        }
        if (Hibernate.isInitialized(userEntity.getRoleRealms()) && userEntity.getRoleRealms() != null) {
            for (SecurityUserRoleRealmEntity roleRealmEntity : userEntity.getRoleRealms()) {
                if (roleRealmEntity.getRole().getId() == RoleCategoryType.SUPER_ADMIN.getCode()) {
                    this.setSuperAdmin(true);
                }
                for (SecurityPermissionEntity permissionEntity : roleRealmEntity.getRole().getPermissions()) {
                    this.permissions.add(permissionEntity.getName());
                }
            }
        }

    }

}
