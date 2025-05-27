package com.armin.operation.admin.model.dto.account;

import com.armin.utility.object.IValidation;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserShahkarIn implements IValidation {
    @NotNull
    private String nationalId;
    @NotNull
    private LocalDate birthDate;

    @Override
    public void validate() throws SystemException {
        if (birthDate != null && birthDate.isAfter(LocalDate.now())) {
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "Wrong BirthDate", 162);
        }
    }
}
