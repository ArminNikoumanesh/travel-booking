package com.armin.utility.object;

import com.armin.utility.bl.StringService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
public class UserContextDto {
    private Integer id;
    private Integer sessionId;
    private String fullName;
    private boolean suspended;
    private LocalDateTime lockExpired;
    private boolean mobileConfirmed;
    private boolean emailConfirmed;
    private Set<Integer> permissionIds;

    public UserContextDto(Integer id, Integer sessionId, String fullName, boolean suspended, Timestamp lockExpired, boolean mobileConfirmed, boolean emailConfirmed, String permissionIds) {
        this.id = id;
        this.sessionId = sessionId;
        this.fullName = fullName;
        this.suspended = suspended;
        if (lockExpired != null) {
            this.lockExpired = lockExpired.toLocalDateTime();
        }
        this.mobileConfirmed = mobileConfirmed;
        this.emailConfirmed = emailConfirmed;
        this.permissionIds = StringService
                .convertCommaSeparatedStringToList(permissionIds)
                .stream()
                .map(Integer::valueOf)
                .collect(Collectors.toSet());
    }

    public void setLockExpired(Timestamp lockExpired) {
        if (lockExpired != null) {
            this.lockExpired = lockExpired.toLocalDateTime();
        }
    }

    public void setPermissionIds(String permissionIds) {
        this.permissionIds = StringService
                .convertCommaSeparatedStringToList(permissionIds)
                .stream()
                .map(Integer::valueOf)
                .collect(Collectors.toSet());
    }
}
