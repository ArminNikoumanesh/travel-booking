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
public class RegistrationOptions implements Diffable<RegistrationOptions> {
    private boolean registerEnabled = true;
    private boolean registerByOtp = true;
    private boolean showFirstName = true;
    private boolean showLastName = true;
    private boolean showEmail = false;
    private boolean showMobile = true;
    private boolean showNationalId = false;
    private boolean showEconomicCode = false;
    private boolean showBirthDate = false;
    private boolean showGender = false;
    private boolean showReferrerMobile = true;
    private boolean requireFirstName = true;
    private boolean requireLastName = true;
    private boolean requireEmail = false;
    private boolean requireMobile = true;
    private boolean requireNationalId = false;
    private boolean requireEconomicCode = false;
    private boolean requireBirthDate = false;
    private boolean requireGender = false;
    private boolean requireReferrerMobile = true;


    public RegistrationOptions(RegistrationOptions registrationOptions) {
        this.registerEnabled = registrationOptions.isRegisterEnabled();
        this.registerByOtp = registrationOptions.isRegisterByOtp();
        this.showFirstName = registrationOptions.isShowFirstName();
        this.showLastName = registrationOptions.isShowLastName();
        this.showEmail = registrationOptions.isShowEmail();
        this.showMobile = registrationOptions.isShowMobile();
        this.showNationalId = registrationOptions.isShowNationalId();
        this.showEconomicCode = registrationOptions.isShowEconomicCode();
        this.showBirthDate = registrationOptions.isShowBirthDate();
        this.showGender = registrationOptions.isShowGender();
        this.showReferrerMobile = registrationOptions.isShowReferrerMobile();
        this.requireFirstName = registrationOptions.isRequireFirstName();
        this.requireLastName = registrationOptions.isRequireLastName();
        this.requireEmail = registrationOptions.isRequireEmail();
        this.requireMobile = registrationOptions.isRequireMobile();
        this.requireNationalId = registrationOptions.isRequireNationalId();
        this.requireEconomicCode = registrationOptions.isRequireEconomicCode();
        this.requireBirthDate = registrationOptions.isRequireBirthDate();
        this.requireGender = registrationOptions.isRequireGender();
        this.requireReferrerMobile = registrationOptions.isRequireReferrerMobile();
    }

    public void validateDetails() {
        if (this.requireFirstName) {
            this.showFirstName = true;
        }
        if (this.requireLastName) {
            this.showLastName = true;
        }
        if (this.requireEmail) {
            this.showEmail = true;
        }
        if (this.requireMobile) {
            this.showMobile = true;
        }
        if (this.requireNationalId) {
            this.showNationalId = true;
        }
        if (this.requireEconomicCode) {
            this.showEconomicCode = true;
        }
        if (this.requireBirthDate) {
            this.showBirthDate = true;
        }
        if (this.requireGender) {
            this.showGender = true;
        }
        if (this.requireReferrerMobile) {
            this.showReferrerMobile = true;
        }
    }


    @Override
    public DiffResult<RegistrationOptions> diff(RegistrationOptions registrationOptions) {
        return new DiffBuilder(this, registrationOptions, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("registerEnabled", this.registerEnabled, registrationOptions.isRegisterEnabled())
                .append("registerByOtp", this.registerByOtp, registrationOptions.isRegisterByOtp())
                .append("showFirstName", this.showFirstName, registrationOptions.isShowFirstName())
                .append("showLastName", this.showLastName, registrationOptions.isShowLastName())
                .append("showEmail", this.showEmail, registrationOptions.isShowEmail())
                .append("showMobile", this.showMobile, registrationOptions.isShowMobile())
                .append("showNationalId", this.showNationalId, registrationOptions.isShowNationalId())
                .append("showEconomicCode", this.showEconomicCode, registrationOptions.isShowEconomicCode())
                .append("showBirthDate", this.showBirthDate, registrationOptions.isShowBirthDate())
                .append("showGender", this.showGender, registrationOptions.isShowGender())
                .append("showReferrerMobile", this.showReferrerMobile, registrationOptions.isShowReferrerMobile())
                .append("requireFirstName", this.requireFirstName, registrationOptions.isRequireFirstName())
                .append("requireLastName", this.requireLastName, registrationOptions.isRequireLastName())
                .append("requireEmail", this.requireEmail, registrationOptions.isRequireEmail())
                .append("requireMobile", this.requireMobile, registrationOptions.isRequireMobile())
                .append("requireNationalId", this.requireNationalId, registrationOptions.isRequireNationalId())
                .append("requireEconomicCode", this.requireEconomicCode, registrationOptions.isRequireEconomicCode())
                .append("requireBirthDate", this.requireBirthDate, registrationOptions.isRequireBirthDate())
                .append("requireGender", this.requireGender, registrationOptions.isRequireGender())
                .append("requireReferrerMobile", this.requireReferrerMobile, registrationOptions.isRequireReferrerMobile())
                .build();
    }
}
