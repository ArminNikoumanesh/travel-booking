package com.armin.operation.admin.model.dto.common;

import com.armin.database.user.entity.UserEntity;
import com.armin.database.user.statics.UserMedium;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserManageOut extends UserProfileOut {
    private boolean suspend;
    private LocalDateTime lockExpired;
    private UserMedium medium;

    public UserManageOut() {
        super();
    }

    public UserManageOut(UserEntity userEntity) {
        super(userEntity);
        this.medium = userEntity.getMedium();
        this.suspend = userEntity.isSuspended();
        this.lockExpired = userEntity.getLockExpired();
    }
}
