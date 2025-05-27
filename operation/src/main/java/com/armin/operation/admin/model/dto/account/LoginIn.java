package com.armin.operation.admin.model.dto.account;

import com.armin.security.statics.constants.ClientType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static com.armin.utility.bl.NormalizeEngine.normalizePersianString;

public class LoginIn {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String uniqueId;
    private String firebaseToken;
    @NotNull
    private ClientType clientType;
    @NotBlank
    private String token;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = normalizePersianString(username);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = normalizePersianString(password);
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

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
