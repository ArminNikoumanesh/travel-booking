package com.armin.operation.security.ident.model.dto;

import com.armin.security.statics.constants.ClientType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserSessionIn {
    @NotNull
    @Size(max = 100)
    private String uniqueId;
    @NotNull
    private ClientType clientType;
    @NotNull
    @Size(max = 50)
    private String os;
    @NotNull
    @Size(max = 100)
    private String agent;
    private String firebaseToken;
    @NotNull
    @Size(max = 39)
    private String ip;

}
