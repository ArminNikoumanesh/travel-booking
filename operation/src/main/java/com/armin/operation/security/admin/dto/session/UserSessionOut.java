package com.armin.operation.security.admin.dto.session;

import com.armin.utility.config.factory.StaticApplicationContext;
import com.armin.database.user.entity.UserSessionEntity;
import com.armin.operation.security.admin.dto.UserInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserSessionOut extends UserSessionIn {
    private int id;
    private LocalDateTime created;
    @Setter(AccessLevel.PRIVATE)
    private UserInfo user;

    public UserSessionOut(UserSessionEntity entity) {
        if (entity != null) {
            ModelMapper mapper = StaticApplicationContext.getContext().getBean(ModelMapper.class);
            mapper.map(entity, this);
            if (Hibernate.isInitialized(entity.getUser()) && entity.getUser() != null) {
                user = new UserInfo(entity.getUser());
            }

        }
    }
}
