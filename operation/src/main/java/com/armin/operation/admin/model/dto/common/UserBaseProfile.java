package com.armin.operation.admin.model.dto.common;

import com.armin.utility.config.cloud.BaseApplicationProperties;
import com.armin.utility.config.factory.StaticApplicationContext;
import com.armin.utility.object.ErrorResult;
import com.armin.utility.object.IValidation;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.armin.utility.bl.NormalizeEngine.normalizePersianString;

/**
 * @author : Armin.Nik
 * @project : chabok-pay
 * @date : 08.09.24
 */
@Getter
@Setter
public class UserBaseProfile implements IValidation {
    private String image;
    private String password;
    private String nationalId;
    @NotBlank
    private LocalDate birthDate;

    public void setPassword(String password) {
        this.password = normalizePersianString(password);
    }


    @Override
    public void validate() throws SystemException {
        BaseApplicationProperties applicationProperties = StaticApplicationContext.getContext().getBean(BaseApplicationProperties.class);
        List<ErrorResult> errorResults = new ArrayList<>();

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

        if (this.nationalId == null || nationalId.length() != 10) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "nationalId length validation", 3468));
            errorResults.add(errorResult);
        }

        if (!errorResults.isEmpty()) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "multiple validation exception on class UserRegistrationIn", 3467, errorResults);
        }
    }
}
