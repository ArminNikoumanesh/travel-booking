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
@JsonDeserialize(as = TextField.class)
public class TextField extends FieldBase {
    @Size(max = 50)
    private String defaultValue;
    @Size(max = 50)
    private String regex;
    private Boolean multiLine = false;
    private Integer maxTextLength;

}
