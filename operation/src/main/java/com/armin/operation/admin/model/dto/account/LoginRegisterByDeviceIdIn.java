package com.armin.operation.admin.model.dto.account;

import com.armin.security.statics.constants.ClientType;

import jakarta.validation.constraints.NotNull;

public class LoginRegisterByDeviceIdIn {
    @NotNull
    private String deviceId;
    @NotNull
    private String uniqueId;
    private String firebaseToken;
    @NotNull
    private ClientType clientType;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }
}
