package com.armin.infrastructure.utility.fields.model;

import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Getter
@Setter
@JsonDeserialize(as = LongField.class)
public class LongField extends FieldBase {
    private Long defaultValue;
    private String unit;
    private Long min;
    private Long max;

    @Override
    public void validate() throws SystemException {
        if (min != null && max != null && max <= min)
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "max min comparision on Long Field Validation", 1234, null);
    }
}
