package com.armin.infrastructure.utility.fields.model;

import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Setter
@Getter
@NoArgsConstructor
@JsonDeserialize(as = DoubleField.class)
@Validated
public class DoubleField extends FieldBase {
    private Double defaultValue;
    @Size(max = 50)
    private String unit;
    private Double min;
    private Double max;
    @NotNull
    private Integer decimalPlaces = 2;

    @Override
    public void validate() throws SystemException {
        if (min != null && max != null && max <= min)
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "max min comparision on Long Field Validation", 1234, null);

    }
}
