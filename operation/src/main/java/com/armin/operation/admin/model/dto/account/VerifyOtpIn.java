package com.armin.operation.admin.model.dto.account;

import com.armin.security.statics.constants.ClientType;
import com.armin.utility.bl.NormalizeEngine;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class VerifyOtpIn {
    @NotNull
    private String key;
    @NotNull
    private String code;
    @NotNull
    private String uniqueId;
    @NotNull
    private ClientType clientType;
    private String deviceId;

    public VerifyOtpIn(@NotNull String key, @NotNull String code) {
        this.key = key;
        this.code = code;
    }

    public void setCode(String code) {
        this.code = NormalizeEngine.normalizePersianDigits(code);
    }
}
