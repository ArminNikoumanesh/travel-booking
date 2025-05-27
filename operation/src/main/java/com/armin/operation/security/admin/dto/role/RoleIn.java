package com.armin.operation.security.admin.dto.role;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.armin.utility.bl.NormalizeEngine.normalizePersianString;

/**
 * @author imax on 5/18/19
 */

@Getter
@Setter
public class RoleIn {
    @NotNull
    @Size(max = 50)
    private String name;
    private Integer[] permissionIds;

    public void setName(String name) {
        this.name = normalizePersianString(name);
    }

}
