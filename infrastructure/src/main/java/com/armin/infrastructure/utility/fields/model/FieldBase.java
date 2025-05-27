package com.armin.infrastructure.utility.fields.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.armin.infrastructure.utility.fields.FieldsDeserializer;
import com.armin.utility.object.IValidation;
import com.armin.utility.object.SystemException;
import com.armin.infrastructure.utility.fields.statics.FieldOnType;
import com.armin.infrastructure.utility.fields.statics.FieldType;
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
@Validated
@JsonDeserialize(using = FieldsDeserializer.class)
public abstract class FieldBase implements IValidation {
    private String id;
    @NotNull
//    @Pattern(regexp = "^[a-zA-Z0-9_]+$")
    private String name;
    @NotNull
    private String displayName;
    @NotNull
    private FieldType type;
    @Size(max = 50)
    private String viewName;
    @Size(max = 2000)
    private String viewData;
    private boolean required = false;
    private boolean filterable = false;
    @NotNull
    private FieldOnType fieldOn;

    public void validate() throws SystemException {
    }

}
