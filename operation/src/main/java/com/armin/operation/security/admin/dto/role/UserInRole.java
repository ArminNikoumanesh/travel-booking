package com.armin.operation.security.admin.dto.role;

import jakarta.validation.constraints.NotNull;

/**
 * @author imax on 5/18/19
 */
public class UserInRole {

    @NotNull
    private Integer userId;
    @NotNull
    private Integer roleId;
    private Integer realmId;

    public UserInRole(@NotNull Integer userId, @NotNull Integer roleId, Integer realmId) {
        this.userId = userId;
        this.roleId = roleId;
        this.realmId = realmId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getRealmId() {
        return realmId;
    }

    public void setRealmId(int realmId) {
        this.realmId = realmId;
    }
}
