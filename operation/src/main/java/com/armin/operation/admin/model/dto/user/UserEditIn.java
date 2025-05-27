package com.armin.operation.admin.model.dto.user;

import com.armin.utility.config.factory.StaticApplicationContext;
import com.armin.utility.config.cloud.BaseApplicationProperties;
import com.armin.utility.object.ErrorResult;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.operation.admin.model.dto.common.UserProfileIn;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Exceptions error code range: 3481-3500
 */

@Getter
@Setter
public class UserEditIn extends UserProfileIn {
    @Size(min = 6, max = 50)
    private String password;
    private String passwordConfirm;

    @NotNull
    private Boolean twoFactorEnabled;
    private Integer userLevelId;
    private Short extension;

    @Override
    public void validate() throws SystemException {
        super.validate();

        BaseApplicationProperties applicationProperties = StaticApplicationContext.getContext().getBean(BaseApplicationProperties.class);
        List<ErrorResult> errorResults = new ArrayList<>();

        if (this.getUser().getLegal() && this.getUser().getNationalId() != null && this.getUser().getNationalId().length() != 11) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "nationalId length for legal is incorrect", 3509));
            errorResults.add(errorResult);
        }
        if (this.getUser().getLegal()) {
            if (this.getProfile().getEconomicCode() != null && getProfile().getEconomicCode().length() != 12) {
                ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "economicCode length must be 12", 3510));
                errorResults.add(errorResult);
            }
        }

        if (this.password != null) {

            if (!Objects.equals(this.password, this.passwordConfirm)) {
                throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "password", 100);
            }

            if (this.passwordConfirm.length() < applicationProperties.getIdentitySettings().getPassword().getRequiredLength()) {
                ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword length validation", 3481));
                errorResults.add(errorResult);
            }

            if (this.passwordConfirm.chars().distinct().count() < applicationProperties.getIdentitySettings().getPassword().getRequiredUniqueChars()) {
                ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword unique character validation", 3482));
                errorResults.add(errorResult);
            }

            if (applicationProperties.getIdentitySettings().getPassword().isRequireNonAlphaNumeric() && this.passwordConfirm.chars().allMatch(Character::isLetterOrDigit)) {
                ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword non alpha numeric validation", 3483));
                errorResults.add(errorResult);
            }

            if (applicationProperties.getIdentitySettings().getPassword().isRequireDigit() && this.passwordConfirm.chars().noneMatch(Character::isDigit)) {
                ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword digit validation", 3484));
                errorResults.add(errorResult);
            }

            if (applicationProperties.getIdentitySettings().getPassword().isRequireLowercase() && this.passwordConfirm.chars().noneMatch(Character::isLowerCase)) {
                ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword lower case validation", 3485));
                errorResults.add(errorResult);
            }

            if (applicationProperties.getIdentitySettings().getPassword().isRequireUppercase() && this.passwordConfirm.chars().noneMatch(Character::isUpperCase)) {
                ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword upper case validation", 3486));
                errorResults.add(errorResult);
            }
        }

        if (!errorResults.isEmpty()) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "multiple validation exception on class UserEditIn", 3487, errorResults);
        }
    }
}
