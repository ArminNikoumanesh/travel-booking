package com.armin.operation.admin.model.dto.account;

import com.armin.utility.config.cloud.BaseApplicationProperties;
import com.armin.utility.config.factory.StaticApplicationContext;
import com.armin.utility.object.ErrorResult;
import com.armin.utility.object.IValidation;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.armin.utility.bl.NormalizeEngine.normalizePersianString;

/**
 * @author : Armin.Nik
 * @project : chabok-pay
 * @date : 16.10.24
 */
public class ChangeForgotPassword implements IValidation {
    @NotNull
    private String username;
    @NotNull
    @Size(min = 6, max = 50)
    private String newPassword;
    @NotNull
    private String newPasswordConfirm;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = normalizePersianString(newPassword);
    }

    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = normalizePersianString(newPasswordConfirm);
    }

    @Override
    public void validate() throws SystemException {
        BaseApplicationProperties applicationProperties = StaticApplicationContext.getContext().getBean(BaseApplicationProperties.class);
        List<ErrorResult> errorResults = new ArrayList<>();

        if (this.newPassword.length() < applicationProperties.getIdentitySettings().getPassword().getRequiredLength()) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword length validation", 3421));
            errorResults.add(errorResult);
        }

        if (this.newPassword.chars().distinct().count() < applicationProperties.getIdentitySettings().getPassword().getRequiredUniqueChars()) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword unique character validation", 3422));
            errorResults.add(errorResult);
        }

        if (applicationProperties.getIdentitySettings().getPassword().isRequireNonAlphaNumeric() && this.newPassword.chars().allMatch(Character::isLetterOrDigit)) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword non alpha numeric validation", 3423));
            errorResults.add(errorResult);
        }

        if (applicationProperties.getIdentitySettings().getPassword().isRequireDigit() && this.newPassword.chars().noneMatch(Character::isDigit)) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword digit validation", 3424));
            errorResults.add(errorResult);
        }

        if (applicationProperties.getIdentitySettings().getPassword().isRequireLowercase() && this.newPassword.chars().noneMatch(Character::isLowerCase)) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword lower case validation", 3425));
            errorResults.add(errorResult);
        }

        if (applicationProperties.getIdentitySettings().getPassword().isRequireUppercase() && this.newPassword.chars().noneMatch(Character::isUpperCase)) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword upper case validation", 3426));
            errorResults.add(errorResult);
        }
        if (!errorResults.isEmpty()) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "multiple validation exception on class ChangedPasswordIn", 3427, errorResults);
        }

        if (!Objects.equals(newPassword, newPasswordConfirm)) {
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "passwords do not match", 3428);
        }
    }
}
