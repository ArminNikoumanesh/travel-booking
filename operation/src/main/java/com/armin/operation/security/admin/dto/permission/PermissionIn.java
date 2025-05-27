package com.armin.operation.security.admin.dto.permission;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author imax on 5/16/19
 */
@Getter
@Setter
public class PermissionIn {

    @NotNull
    @Size(max = 50)
    private String name;
    @NotNull
    private Integer nodeType;
    @NotNull
    private Boolean traversal;
    private Integer parentIdFk;
    private Integer[] endPointIds;
}
