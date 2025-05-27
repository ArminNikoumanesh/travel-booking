package com.armin.operation.admin.model.dto.account;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class CheckUserExistsIn {
    @NotNull
    private String username;
}
