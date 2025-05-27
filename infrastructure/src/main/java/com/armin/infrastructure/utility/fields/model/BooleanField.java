package com.armin.infrastructure.utility.fields.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Size;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Getter
@Setter
@JsonDeserialize(as = BooleanField.class)
public class BooleanField extends FieldBase {
    private Boolean defaultValue;
    @Size(max = 50)
    private String trueLabel;
    @Size(max = 50)
    private String falseLabel;

}
