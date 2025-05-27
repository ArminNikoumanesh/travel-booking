package com.armin.operation.admin.model.dto.profile;

import com.armin.database.user.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

@Getter
@Setter
public class ProfileImageOut {
    private String image;

    public ProfileImageOut(UserEntity userEntity) {
        if (Hibernate.isInitialized(userEntity.getProfile()) && userEntity.getProfile() != null) {
            this.image = userEntity.getProfile().getImage();
        }
    }
}
