package com.armin.operation.admin.model.dto.user;

import com.armin.utility.object.ErrorResult;
import com.armin.utility.object.IValidation;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static com.armin.utility.bl.NormalizeEngine.getNormalizedPhoneNumber;
import static com.armin.utility.bl.NormalizeEngine.normalizePersianString;

/**
 * Exceptions error code range: 3501-3520
 */
@Getter
@Setter
public class UserIn implements IValidation {
    private String mobile;
    @Email
    private String email;
    @NotNull
    private Boolean legal;
    @Size(max = 100)
    private String firstName;
    @Size(max = 100)
    private String lastName;
    @Size(max = 100)
    private String legalName;
    @Size(max = 11)
    private String nationalId;
    private boolean twoFactorEnabled;

    public void setEmail(String email) {
        this.email = normalizePersianString(email);
    }

    public void setFirstName(String firstName) {
        this.firstName = normalizePersianString(firstName);
    }

    public void setLastName(String lastName) {
        this.lastName = normalizePersianString(lastName);
    }

    public void setLegalName(String legalName) {
        this.legalName = normalizePersianString(legalName);
    }

    public void setNationalId(String nationalId) {
        this.nationalId = normalizePersianString(nationalId);
    }

    @Override
    public void validate() throws SystemException {
        if (this.mobile != null && this.mobile.isEmpty()) {
            this.mobile = null;
        }
        if (this.email != null && this.email.isEmpty()) {
            this.email = null;
        }
        this.mobile = getNormalizedPhoneNumber(this.mobile);
        if (this.getMobile() == null && this.getEmail() == null) {
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "mobile,email", 3501);

        }
        if (this.email != null) {
            setEmail(this.email);
        }
        if (this.firstName != null) {
            setFirstName(this.firstName);
        }
        if (this.lastName != null) {
            setLastName(this.lastName);
        }
        if (this.legalName != null) {
            setLegalName(this.legalName);
        }
        if (this.nationalId != null) {
            setNationalId(this.nationalId);
        }
        List<ErrorResult> errorResults = new ArrayList<>();

        if (!errorResults.isEmpty()) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "multiple validation exception on class UserIn", 3508, errorResults);
        }

    }

}
