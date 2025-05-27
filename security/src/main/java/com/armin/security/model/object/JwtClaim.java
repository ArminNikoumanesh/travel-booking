package com.armin.security.model.object;


public class JwtClaim {
    private Integer userId;
    private Integer sessionId;

    public JwtClaim(Integer userId, Integer sessionId) {
        this.userId = userId;
        this.sessionId = sessionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

}
