package com.armin.operation.admin.model.dto.account;

import com.armin.database.user.entity.UserEntity;
import com.armin.operation.admin.model.dto.common.UserProfileInfoOut;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AccountInfoOut extends UserProfileInfoOut {
    private LocalDate birthDate;
    private String mobile;
    private String email;
    private boolean mobileConfirmed;
    private boolean emailConfirmed;
    private boolean verified;
    private boolean registered;


    public AccountInfoOut(UserEntity userEntity) {
        super(userEntity);
        this.birthDate = userEntity.getProfile().getBirthDate();
        this.mobile = userEntity.getMobile();
        this.email = userEntity.getEmail();
        this.mobileConfirmed = userEntity.isMobileConfirmed();
        this.emailConfirmed = userEntity.isEmailConfirmed();
        this.setImage(userEntity.getProfile().getImage());
        this.setFirstName(userEntity.getFirstName());
        this.setLastName(userEntity.getLastName());
        this.setLegal(userEntity.isLegal());
        this.setLegalName(userEntity.getLegalName());
        this.setId(userEntity.getId());
        this.setGender(userEntity.getProfile().getGender());
        this.setVerified(userEntity.isVerified());
        this.setRegistered(userEntity.isRegistered());
    }

}
