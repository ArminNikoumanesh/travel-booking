package com.armin.utility.config.cloud.identity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@Setter
@NoArgsConstructor
public class JwtOptions implements Diffable<JwtOptions> {
    private long mobileAccessTokenExpireMin;
    private long browserAccessTokenExpireMin;
    private long mobileRefreshTokenExpireMin;
    private long browserRefreshTokenExpireMin;
    private String refreshPath;
    private String securityKey;

    public JwtOptions(JwtOptions jwtOptions) {
        this.mobileAccessTokenExpireMin = jwtOptions.getMobileAccessTokenExpireMin();
        this.browserAccessTokenExpireMin = jwtOptions.getBrowserAccessTokenExpireMin();
        this.mobileRefreshTokenExpireMin = jwtOptions.getMobileRefreshTokenExpireMin();
        this.browserRefreshTokenExpireMin = jwtOptions.getBrowserRefreshTokenExpireMin();
        this.refreshPath = jwtOptions.getRefreshPath();
        this.securityKey = jwtOptions.getSecurityKey();
    }

    @Override
    public DiffResult<JwtOptions> diff(JwtOptions jwtOptions) {
        return new DiffBuilder(this, jwtOptions, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("mobileAccessTokenExpireMin", this.mobileAccessTokenExpireMin, jwtOptions.getMobileAccessTokenExpireMin())
                .append("browserAccessTokenExpireMin", this.browserAccessTokenExpireMin, jwtOptions.getBrowserAccessTokenExpireMin())
                .append("mobileRefreshTokenExpireMin", this.mobileRefreshTokenExpireMin, jwtOptions.getMobileRefreshTokenExpireMin())
                .append("browserRefreshTokenExpireMin", this.browserRefreshTokenExpireMin, jwtOptions.getBrowserRefreshTokenExpireMin())
                .append("refreshPath", this.refreshPath, jwtOptions.getRefreshPath())
                .append("securityKey", this.securityKey, jwtOptions.getSecurityKey())
                .build();
    }
}
