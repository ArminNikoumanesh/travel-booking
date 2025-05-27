package com.armin.utility.object;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.servlet.http.HttpServletRequest;

@Setter
@Getter
@NoArgsConstructor
public class ClientIpInfo {
    protected String mainIP;
    protected String proxyIP;

    public ClientIpInfo(HttpServletRequest request) {
        this.mainIP = request.getHeader("X-FORWARDED-FOR");
        this.proxyIP = request.getRemoteAddr();
        if (this.mainIP == null || this.mainIP.equals("")) {
            this.mainIP = request.getRemoteAddr();
            this.proxyIP = null;
        } else {
            mainIP = mainIP.split(",")[0];
        }
    }

    public static String getMainIP(HttpServletRequest request) {
        String mainIP = request.getHeader("X-FORWARDED-FOR");
        if (mainIP == null || mainIP.equals("")) {
            mainIP = request.getRemoteAddr();
        } else {
            mainIP = mainIP.split(",")[0];
        }
        return mainIP;
    }

    @Override
    public String toString() {
        return "ClientIpInfo{" +
                "mainIP='" + mainIP + '\'' +
                ", proxyIP='" + proxyIP + '\'' +
                '}';
    }
}
