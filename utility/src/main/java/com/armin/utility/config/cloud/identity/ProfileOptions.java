package com.armin.utility.config.cloud.identity;

import com.armin.utility.object.IValidation;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
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
public class ProfileOptions implements Diffable<ProfileOptions>, IValidation {
    private boolean showFirstName = true;
    private boolean showLastName = true;
    private boolean showEmail = false;
    private boolean showMobile = true;
    private boolean showNationalId = false;
    private boolean showEconomicCode = false;
    private boolean showBirthDate = false;
    private boolean showGender = false;
    private boolean requireFirstName = true;
    private boolean requireLastName = true;
    private boolean requireEmail = false;
    private boolean requireMobile = true;
    private boolean requireNationalId = false;
    private boolean requireEconomicCode = false;
    private boolean requireBirthDate = false;
    private boolean requireGender = false;
    private boolean changeNationalId = true;
    private boolean birthDateLimit = false;
    private Integer birthDateLimitMinimum = null;
    private Integer birthDateLimitMaximum = null;

    public ProfileOptions(ProfileOptions profileOptions) {
        this.showFirstName = profileOptions.isShowFirstName();
        this.showLastName = profileOptions.isShowLastName();
        this.showEmail = profileOptions.isShowEmail();
        this.showMobile = profileOptions.isShowMobile();
        this.showNationalId = profileOptions.isShowNationalId();
        this.showEconomicCode = profileOptions.isShowEconomicCode();
        this.showBirthDate = profileOptions.isShowBirthDate();
        this.showGender = profileOptions.isShowGender();
        this.requireFirstName = profileOptions.isRequireFirstName();
        this.requireLastName = profileOptions.isRequireLastName();
        this.requireEmail = profileOptions.isRequireEmail();
        this.requireMobile = profileOptions.isRequireMobile();
        this.requireNationalId = profileOptions.isRequireNationalId();
        this.requireEconomicCode = profileOptions.isRequireEconomicCode();
        this.requireBirthDate = profileOptions.isRequireBirthDate();
        this.requireGender = profileOptions.isRequireGender();
        this.changeNationalId = profileOptions.isChangeNationalId();
        this.birthDateLimit = profileOptions.isBirthDateLimit();
        this.birthDateLimitMinimum = profileOptions.getBirthDateLimitMinimum();
        this.birthDateLimitMaximum = profileOptions.getBirthDateLimitMaximum();
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
    }

    @Override
    public void validate() throws SystemException {
        if ((this.birthDateLimitMinimum != null && this.birthDateLimitMaximum != null) &&
                (Integer.compare(birthDateLimitMaximum, birthDateLimitMinimum) != 1)) {
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "maximum must greater than minimum", 2912);
        }
    }

    @Override
    public DiffResult<ProfileOptions> diff(ProfileOptions profileOptions) {
        return new DiffBuilder(this, profileOptions, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("showFirstName", this.showFirstName, profileOptions.isShowFirstName())
                .append("showLastName", this.showLastName, profileOptions.isShowLastName())
                .append("showEmail", this.showEmail, profileOptions.isShowEmail())
                .append("showMobile", this.showMobile, profileOptions.isShowMobile())
                .append("showNationalId", this.showNationalId, profileOptions.isShowNationalId())
                .append("showEconomicCode", this.showEconomicCode, profileOptions.isShowEconomicCode())
                .append("showBirthDate", this.showBirthDate, profileOptions.isShowBirthDate())
                .append("showGender", this.showGender, profileOptions.isShowGender())
                .append("requireFirstName", this.requireFirstName, profileOptions.isRequireFirstName())
                .append("requireLastName", this.requireLastName, profileOptions.isRequireLastName())
                .append("requireEmail", this.requireEmail, profileOptions.isRequireEmail())
                .append("requireMobile", this.requireMobile, profileOptions.isRequireMobile())
                .append("requireNationalId", this.requireNationalId, profileOptions.isRequireNationalId())
                .append("requireEconomicCode", this.requireEconomicCode, profileOptions.isRequireEconomicCode())
                .append("requireBirthDate", this.requireBirthDate, profileOptions.isRequireBirthDate())
                .append("requireGender", this.requireGender, profileOptions.isRequireGender())
                .append("changeNationalId", this.changeNationalId, profileOptions.isChangeNationalId())
                .append("birthDateLimit", this.birthDateLimit, profileOptions.isBirthDateLimit())
                .append("birthDateLimitMinimum", this.birthDateLimitMinimum, profileOptions.getBirthDateLimitMinimum())
                .append("birthDateLimitMaximum", this.birthDateLimitMaximum, profileOptions.getBirthDateLimitMaximum())
                .build();
    }

}
