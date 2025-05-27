package com.armin.security.model.object;


import com.armin.security.statics.constants.ClientType;

public class AuthRequestHeader {
    private String token;
    private Long clientSerialNumber;
    private ClientType clientType;

    public AuthRequestHeader(String token, Long clientSerialNumber, ClientType clientType) {
        this.token = token;
        this.clientSerialNumber = clientSerialNumber;
        this.clientType = clientType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getClientSerialNumber() {
        return clientSerialNumber;
    }

    public void setClientSerialNumber(Long clientSerialNumber) {
        this.clientSerialNumber = clientSerialNumber;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }
}
