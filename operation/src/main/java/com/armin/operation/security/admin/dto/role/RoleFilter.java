package com.armin.operation.security.admin.dto.role;


import com.armin.utility.repository.orm.service.FilterBase;
import com.armin.database.user.statics.RoleType;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Size;


/**
 * @author imax on 5/18/19
 * @author Mohammad Yasin Sadeghi
 */

@Getter
@Setter
public class RoleFilter implements FilterBase {
    private Integer id;
    @Size(max = 50)
    private String name;
    private Integer permissionId;
    private RoleType type;

}
