package com.armin.operation.security.admin.dto.session;

import com.armin.security.statics.constants.ClientType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserSessionIn {
    @NotNull
    private Integer userId;
    @NotNull
    @Size(max = 100)
    private String uniqueId;
    @NotNull
    private ClientType clientType;
    @NotNull
    private String os;
    @NotNull
    private String agent;
    private String firebaseToken;
    @NotNull
    private String ip;

}
