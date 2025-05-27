package com.armin.operation.admin.model.dto.account;

import com.armin.utility.bl.NormalizeEngine;
import com.armin.utility.object.IValidation;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SendOtpIn implements IValidation {
    @NotNull
    private Boolean sendViaSms;
    private String mobile;
    @NotNull
    private Boolean sendViaEmail;
    @Email
    private String email;
    @NotBlank
    private String token;

    public Boolean getSendViaSms() {
        return sendViaSms;
    }

    public void setSendViaSms(Boolean sendViaSms) {
        this.sendViaSms = sendViaSms;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) throws SystemException {
        this.mobile = NormalizeEngine.getNormalizedPhoneNumber(mobile);
    }

    public Boolean getSendViaEmail() {
        return sendViaEmail;
    }

    public void setSendViaEmail(Boolean sendViaEmail) {
        this.sendViaEmail = sendViaEmail;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void validate() throws SystemException {
        this.mobile = NormalizeEngine.getNormalizedPhoneNumber(this.mobile);
        if (this.sendViaSms && this.mobile == null) {
            throw new SystemException(SystemError.USER_NOT_FOUND, "sms-mobile", 120);
        }
        if (this.sendViaEmail && this.email == null) {
            throw new SystemException(SystemError.USER_NOT_FOUND, "email", 121);
        }
    }
}
