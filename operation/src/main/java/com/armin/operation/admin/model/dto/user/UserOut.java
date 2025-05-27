package com.armin.operation.admin.model.dto.user;


import com.armin.operation.security.admin.dto.role.RoleOut;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class UserOut extends UserIn {
    private int id;
    private boolean mobileConfirmed;
    private boolean emailConfirmed;
    private String fullName;
    private LocalDateTime created;
    private boolean verified;
    private boolean registered;
    private Map<String, List<RoleOut>> realms = new HashMap<>();
}
