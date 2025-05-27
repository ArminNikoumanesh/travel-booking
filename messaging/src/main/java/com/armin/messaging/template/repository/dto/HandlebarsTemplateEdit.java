package com.armin.messaging.template.repository.dto;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class HandlebarsTemplateEdit {
    @NotNull
    @NotBlank
    private String content;
}
