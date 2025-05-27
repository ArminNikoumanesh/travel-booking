package com.armin.operation.security.admin.dto.role;


import com.armin.database.user.entity.SecurityRoleEntity;

/**
 * @author imax on 5/18/19
 */
public class RoleOut extends RoleIn {

    private int id;

    public RoleOut() {
    }

    public RoleOut(SecurityRoleEntity entity) {
        this.id = entity.getId();
        this.setName(entity.getName());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
