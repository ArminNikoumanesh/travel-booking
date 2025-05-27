package com.armin.utility.object;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TokenInfo {
    private String accessToken;
    private String refreshToken;
    private Long ttl;
    private Long refreshTtl;
    private LocalDateTime creationTime;

    public TokenInfo() {
    }

    public TokenInfo(String accessToken, String refreshToken, Long ttl, Long refreshTtl) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.ttl = ttl;
        this.refreshTtl = refreshTtl;
        this.creationTime = LocalDateTime.now();
    }
}
