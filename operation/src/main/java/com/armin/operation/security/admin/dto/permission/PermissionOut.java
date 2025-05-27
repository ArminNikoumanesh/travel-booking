package com.armin.operation.security.admin.dto.permission;

import jakarta.validation.constraints.NotNull;

/**
 * @author imax on 5/16/19
 */
public class PermissionOut extends PermissionIn{

    @NotNull
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
