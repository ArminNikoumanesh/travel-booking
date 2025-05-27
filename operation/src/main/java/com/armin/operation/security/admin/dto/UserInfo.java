package com.armin.operation.security.admin.dto;

import com.armin.database.user.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserInfo {
    private Integer id;
    private String fullName;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;

    public UserInfo(UserEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.fullName = entity.getFullName();
            this.firstName = entity.getFirstName();
            this.lastName = entity.getLastName();
            this.email = entity.getEmail();
            this.mobile = entity.getMobile();
        }
    }

    public UserInfo() {
    }
}
