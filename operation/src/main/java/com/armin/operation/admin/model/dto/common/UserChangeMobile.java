package com.armin.operation.admin.model.dto.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : Armin.Nik
 * @project : chabok-pay
 * @date : 12.10.24
 */
@Getter
@Setter
public class UserChangeMobile {
    @NotNull
    private String newUsername;
    @NotNull
    @Size(max = 10, min = 10)
    private String nationalId;
    @NotBlank
    private String token;

}
