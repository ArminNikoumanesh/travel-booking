package com.armin.infrastructure.utility.fields.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Getter
@Setter
@JsonDeserialize(as = SelectField.class)
public class SelectField extends FieldBase {
    private Integer defaultValue;
    @NotNull
    @NotEmpty
    private List<FieldOption> options;
}
