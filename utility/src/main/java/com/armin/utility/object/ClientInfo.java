package com.armin.utility.object;

import jakarta.servlet.http.HttpServletRequest;

public class ClientInfo extends ClientIpInfo {

    private String requestURI;
    private String agent;
    protected String host;

    public ClientInfo() {
    }

    public ClientInfo(HttpServletRequest request) {
        super(request);
        this.requestURI = request.getRequestURI();
        this.agent = request.getHeader("User-Agent");
        //        this.host = request.getRemoteHost();
        this.host = request.getHeader("Host");
    }

    public String getClientOS() {
        final String lowerCaseBrowser = this.agent.toLowerCase();
        if (lowerCaseBrowser.contains("windows")) {
            return "Windows";
        } else if (lowerCaseBrowser.contains("mac")) {
            return "Mac";
        } else if (lowerCaseBrowser.contains("x11")) {
            return "Unix";
        } else if (lowerCaseBrowser.contains("android")) {
            return "Android";
        } else if (lowerCaseBrowser.contains("iphone")) {
            return "IPhone";
        } else {
            return "UnKnown";
        }
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public String getMainIP() {
        return mainIP;
    }

    public void setMainIP(String mainIP) {
        this.mainIP = mainIP;
    }

    public String getProxyIP() {
        return proxyIP;
    }

    public void setProxyIP(String proxyIP) {
        this.proxyIP = proxyIP;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    @Override
    public String toString() {
        return "ClientInfo {" +
                "requestURI='" + requestURI + '\'' +
                ", mainIP='" + mainIP + '\'' +
                ", proxyIP='" + proxyIP + '\'' +
                ", host='" + host + '\'' +
                ", agent='" + agent + '\'' +
                '}';
    }
}
