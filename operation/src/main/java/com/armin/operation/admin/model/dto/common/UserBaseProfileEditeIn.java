package com.armin.operation.admin.model.dto.common;

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

/**
 * @author : Armin.Nik
 * @project : chabok-pay
 * @date : 04.11.24
 */
@Getter
@Setter
public class UserBaseProfileEditeIn implements IValidation {
    @NotBlank
    private String nationalId;
    @NotBlank
    private LocalDate birthDate;

    @Override
    public void validate() throws SystemException {
        List<ErrorResult> errorResults = new ArrayList<>();

        if (this.nationalId == null || nationalId.length() != 10) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "nationalId length validation", 3468));
            errorResults.add(errorResult);
        }

        if (!errorResults.isEmpty()) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "multiple validation exception on class UserRegistrationIn", 3467, errorResults);
        }
    }
}
