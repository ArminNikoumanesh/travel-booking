package com.armin.operation.admin.model.dto.account;

import com.armin.security.statics.constants.ClientType;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class RegisterSimpleIn {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String uniqueId;
    private String firebaseToken;
    @NotNull
    private ClientType clientType;

}
