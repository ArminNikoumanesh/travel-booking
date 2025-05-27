package com.armin.application.cloud.model.security;

import com.armin.utility.config.cloud.identity.PasswordOptions;
import com.armin.utility.config.cloud.identity.JwtOptions;
import com.armin.utility.config.cloud.identity.LockoutOptions;
import com.armin.database.cloud.ApplicationProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
public class SecurityConfig {
    @Valid
    private PasswordOptions password;
    private LockoutOptions lockout;
    private JwtOptions jwt;

    public SecurityConfig(ApplicationProperties applicationProperties) {
        this.password = new PasswordOptions(applicationProperties.getIdentitySettings().getPassword());
        this.lockout = new LockoutOptions(applicationProperties.getIdentitySettings().getLockout());
        this.jwt = new JwtOptions(applicationProperties.getIdentitySettings().getJwt());
    }
}
