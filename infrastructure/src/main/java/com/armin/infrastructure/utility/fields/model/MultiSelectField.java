package com.armin.infrastructure.utility.fields.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Getter
@Setter
@JsonDeserialize(as = MultiSelectField.class)
public class MultiSelectField extends FieldBase {
    private List<Integer> defaultValue;
    @NotEmpty
    private List<FieldOption> options;

}
