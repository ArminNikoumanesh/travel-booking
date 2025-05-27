package com.armin.utility.config.cloud.identity;

import com.armin.utility.config.cloud.BaseApplicationProperties;
import com.armin.utility.object.IValidation;
import com.armin.utility.object.SystemException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import jakarta.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
public class IdentitySettings implements Diffable<IdentitySettings>, IValidation {
    private SignInOptions signIn;
    private RegistrationOptions registration;
    private ProfileOptions profile;
    @Valid
    private PasswordOptions password;
    private LockoutOptions lockout;
    private JwtOptions jwt;

    public IdentitySettings(BaseApplicationProperties applicationProperties) {
        this.signIn = new SignInOptions(applicationProperties.getIdentitySettings().getSignIn());
        this.registration = new RegistrationOptions(applicationProperties.getIdentitySettings().getRegistration());
        this.profile = new ProfileOptions(applicationProperties.getIdentitySettings().getProfile());
        this.password = new PasswordOptions(applicationProperties.getIdentitySettings().getPassword());
        this.lockout = new LockoutOptions(applicationProperties.getIdentitySettings().getLockout());
        this.jwt = new JwtOptions(applicationProperties.getIdentitySettings().getJwt());
    }

    @Override
    public DiffResult<IdentitySettings> diff(IdentitySettings identitySettings) {
        return new DiffBuilder(this, identitySettings, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("signIn", this.signIn.diff(identitySettings.getSignIn()))
                .append("registration", this.registration.diff(identitySettings.getRegistration()))
                .append("profile", this.profile.diff(identitySettings.getProfile()))
                .append("password", this.password.diff(identitySettings.getPassword()))
                .append("lockout", this.lockout.diff(identitySettings.getLockout()))
                .append("jwt", this.jwt.diff(identitySettings.getJwt()))
                .build();
    }

    @Override
    public void validate() throws SystemException {
        profile.validate();
    }
}
