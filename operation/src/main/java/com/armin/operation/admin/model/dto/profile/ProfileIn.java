package com.armin.operation.admin.model.dto.profile;

import com.armin.utility.object.IValidation;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.database.user.statics.Gender;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;

@Getter
@Setter
public class ProfileIn implements IValidation {
    private Gender gender;
    private LocalDate birthDate;
    private String image;
    private String economicCode;

    @Override
    public void validate() throws SystemException {
        if (birthDate != null && birthDate.isAfter(LocalDate.now())){
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "Wrong BirthDate", 160);
        }
    }
}
