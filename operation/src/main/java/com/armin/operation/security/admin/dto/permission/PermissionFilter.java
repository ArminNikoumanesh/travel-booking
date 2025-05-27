package com.armin.operation.security.admin.dto.permission;


import com.armin.utility.bl.NormalizeEngine;
import com.armin.utility.repository.orm.service.FilterBase;
import com.armin.database.user.statics.PermissionType;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Size;

/**
 * @author imax on 5/16/19
 */
@Setter
@Getter
public class PermissionFilter implements FilterBase {
    private Integer id;
    @Size(max = 50)
    private String name;
    private Integer nodeType;
    private Boolean traversal;
    private Integer parentIdFk;
    private Integer roleId;
    private Integer endpointId;
    private PermissionType type;

    public void setName(String name) {
        this.name = NormalizeEngine.getSlug(name);
    }
}
