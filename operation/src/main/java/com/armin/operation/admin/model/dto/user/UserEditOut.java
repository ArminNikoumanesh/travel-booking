package com.armin.operation.admin.model.dto.user;

import com.armin.utility.config.factory.StaticApplicationContext;
import com.armin.database.user.entity.UserEntity;
import com.armin.operation.admin.model.dto.common.UserProfileOut;
import com.armin.operation.admin.model.dto.profile.ProfileOut;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@NoArgsConstructor
public class UserEditOut extends UserProfileOut {
    private Integer userLevelId;

    public UserEditOut(UserEntity userEntity) {
        ModelMapper modelMapper = StaticApplicationContext.getContext().getBean(ModelMapper.class);
        modelMapper.map(userEntity, this.getUser());
        if (Hibernate.isInitialized(userEntity.getProfile())) {
            setProfile(new ProfileOut(userEntity.getProfile()));
        }
    }

}
