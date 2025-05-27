package com.armin.operation.admin.model.dto.user;

import com.armin.database.user.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserInfo {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String fullName;

    public UserInfo(UserEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.firstName = entity.getFirstName();
            this.lastName = entity.getLastName();
            this.email = entity.getEmail();
            this.mobile = entity.getMobile();
            this.fullName = entity.getFullName();
        }
    }
}
