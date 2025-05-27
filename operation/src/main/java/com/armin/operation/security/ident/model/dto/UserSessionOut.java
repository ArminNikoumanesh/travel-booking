package com.armin.operation.security.ident.model.dto;

import com.armin.utility.config.factory.StaticApplicationContext;
import com.armin.database.user.entity.UserSessionEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserSessionOut extends UserSessionIn {
    private int id;
    private LocalDateTime created;
    private boolean current;

    public UserSessionOut(UserSessionEntity entity, int currentSessionId) {
        ModelMapper modelMapper = StaticApplicationContext.getContext().getBean(ModelMapper.class);
        modelMapper.map(entity, this);
        if (currentSessionId == entity.getId()) {
            this.current = true;
        }
    }
}
