package com.armin.application.cloud.model.security;

import com.armin.database.cloud.ApplicationProperties;
import com.armin.utility.config.cloud.identity.ProfileOptions;
import com.armin.utility.config.cloud.identity.RegistrationOptions;
import com.armin.utility.config.cloud.identity.SignInOptions;
import com.armin.utility.object.IValidation;
import com.armin.utility.object.SystemException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AccountConfig implements IValidation {
    private SignInOptions signIn;
    private RegistrationOptions registration;
    private ProfileOptions profile;

    public AccountConfig(ApplicationProperties applicationProperties) {
        this.signIn = new SignInOptions(applicationProperties.getIdentitySettings().getSignIn());
        this.registration = new RegistrationOptions(applicationProperties.getIdentitySettings().getRegistration());
        this.profile = new ProfileOptions(applicationProperties.getIdentitySettings().getProfile());
    }

    @Override
    public void validate() throws SystemException {
        profile.validate();
    }
}
