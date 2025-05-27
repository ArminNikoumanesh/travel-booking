package com.armin.infrastructure.utility.fields.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class FieldOption {
    @NotNull
    private String id;
    @NotNull
    private String text;
    @NotNull
    private String tagValue;
    @NotNull
    private int sortOrder;
}
