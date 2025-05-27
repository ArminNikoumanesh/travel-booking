package com.armin.operation.admin.model.dto.common;

import com.armin.database.user.entity.UserEntity;
import com.armin.database.user.statics.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import jakarta.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
public class UserProfileInfoOut {
    private int id;
    private String firstName;
    private String lastName;
    private String fullName;
    private Gender gender;
    @Size(max = 100)
    private String image;
    private boolean legal;
    private String legalName;
    private String nationalId;
    private String mobile;

    public UserProfileInfoOut(UserEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.legal = entity.isLegal();
            this.mobile = entity.getMobile();
            this.lastName = entity.getLastName();
            this.fullName = entity.getFullName();
            this.firstName = entity.getFirstName();
            this.legalName = entity.getLegalName();
            this.nationalId = entity.getNationalId();
            if (Hibernate.isInitialized(entity.getProfile()) && entity.getProfile() != null) {
                this.gender = entity.getProfile().getGender();
                this.image = entity.getProfile().getImage();
            }
        }
    }
}
