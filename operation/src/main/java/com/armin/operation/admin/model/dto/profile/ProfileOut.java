package com.armin.operation.admin.model.dto.profile;

import com.armin.utility.config.factory.StaticApplicationContext;
import com.armin.database.user.entity.ProfileEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@NoArgsConstructor
public class ProfileOut extends ProfileIn {
    private int id;
    private String fax;
    private String phone;
    private String registrationNo;


    public ProfileOut(ProfileEntity entity) {
        if (entity != null) {
            ModelMapper modelMapper = StaticApplicationContext.getContext().getBean(ModelMapper.class);
            modelMapper.map(entity, this);
        }
    }
}
