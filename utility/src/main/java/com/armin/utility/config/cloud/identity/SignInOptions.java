package com.armin.utility.config.cloud.identity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@Setter
@NoArgsConstructor
public class SignInOptions implements Diffable<SignInOptions> {
    private boolean loginByMobile = true;
    private boolean loginByEmail = true;
    private boolean loginByOtp = true;
    private boolean loginByGoogle = false;
    private boolean loginByPod = true;
    private boolean loginByPodWeb = true;
    private boolean requireConfirmedMobile = false;
    private boolean requireConfirmedEmail = false;

    public SignInOptions(SignInOptions signInOptions) {
        this.loginByMobile = signInOptions.isLoginByMobile();
        this.loginByEmail = signInOptions.isLoginByEmail();
        this.loginByOtp = signInOptions.isLoginByOtp();
        this.loginByGoogle = signInOptions.isLoginByGoogle();
        this.loginByPod = signInOptions.isLoginByPod();
        this.requireConfirmedMobile = signInOptions.isRequireConfirmedMobile();
        this.requireConfirmedEmail = signInOptions.isRequireConfirmedEmail();
    }

    @Override
    public DiffResult<SignInOptions> diff(SignInOptions signInOptions) {
        return new DiffBuilder(this, signInOptions, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("loginByMobile", this.loginByMobile, signInOptions.isLoginByMobile())
                .append("loginByEmail", this.loginByEmail, signInOptions.isLoginByEmail())
                .append("loginByOtp", this.loginByOtp, signInOptions.isLoginByOtp())
                .append("loginByGoogle", this.loginByGoogle, signInOptions.isLoginByGoogle())
                .append("loginByPod", this.loginByPod, signInOptions.isLoginByPod())
                .append("loginByPodWeb", this.loginByPodWeb, signInOptions.isLoginByPodWeb())
                .append("requireConfirmedMobile", this.requireConfirmedEmail, signInOptions.isRequireConfirmedEmail())
                .append("requireConfirmedEmail", this.requireConfirmedEmail, signInOptions.isRequireConfirmedEmail())
                .build();
    }
}
