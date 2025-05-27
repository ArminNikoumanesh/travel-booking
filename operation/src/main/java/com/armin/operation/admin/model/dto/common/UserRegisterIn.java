package com.armin.operation.admin.model.dto.common;

import com.armin.security.statics.constants.ClientType;
import com.armin.utility.config.cloud.BaseApplicationProperties;
import com.armin.utility.config.factory.StaticApplicationContext;
import com.armin.utility.object.ErrorResult;
import com.armin.utility.object.IValidation;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static com.armin.utility.bl.NormalizeEngine.normalizePersianString;


/**
 * Exceptions error code range: 3461-3480
 */
@Setter
@Getter
public class UserRegisterIn extends UserProfileIn implements IValidation {
    @Size(max = 50, min = 6)
    private String password;
    @NotNull
    private ClientType clientType;
    @NotNull
    private String uniqueId;
    private String firebaseToken;
    private Boolean legal;

    public void setPassword(String password) {
        this.password = normalizePersianString(password);
    }

    @Override
    public void validate() throws SystemException {
        super.validate();
        BaseApplicationProperties applicationProperties = StaticApplicationContext.getContext().getBean(BaseApplicationProperties.class);
        List<ErrorResult> errorResults = new ArrayList<>();

        if (applicationProperties.getIdentitySettings().getPassword().isRequireNonAlphaNumeric()) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword non alpha numeric validation", 3463));
            errorResults.add(errorResult);
        }

        if (((this.legal != null && this.legal) || (this.getUser().getLegal() != null && this.getUser().getLegal())) && this.getUser().getNationalId() != null && this.getUser().getNationalId().length() != 11 && applicationProperties.getIdentitySettings().getProfile().isRequireNationalId()) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "nationalId length for legal is incorrect", 3509));
            errorResults.add(errorResult);
        }
        if (this.password.length() < applicationProperties.getIdentitySettings().getPassword().getRequiredLength()) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword length validation", 3461));
            errorResults.add(errorResult);
        }

        if (this.password.chars().distinct().count() < applicationProperties.getIdentitySettings().getPassword().getRequiredUniqueChars()) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword unique character validation", 3462));
            errorResults.add(errorResult);
        }
        if (applicationProperties.getIdentitySettings().getPassword().isRequireDigit() && this.password.chars().noneMatch(Character::isDigit)) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword digit validation", 3464));
            errorResults.add(errorResult);
        }

        if (applicationProperties.getIdentitySettings().getPassword().isRequireLowercase() && this.password.chars().noneMatch(Character::isLowerCase)) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword lower case validation", 3465));
            errorResults.add(errorResult);
        }

        if (applicationProperties.getIdentitySettings().getPassword().isRequireUppercase() && this.password.chars().noneMatch(Character::isUpperCase)) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword upper case validation", 3466));
            errorResults.add(errorResult);
        }
        if (!errorResults.isEmpty()) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "multiple validation exception on class UserRegistrationIn", 3467, errorResults);
        }


    }
}
