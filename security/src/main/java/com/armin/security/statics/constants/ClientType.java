package com.armin.security.statics.constants;

public enum ClientType {
    WEB(0),
    ANDROID(1),
    IOS(2),
    WEB_SERVICE(3);

    private final Integer value;

    ClientType(Integer value) {
        this.value = value;
    }

    public static ClientType fromValue(String type) {
        switch (type.toLowerCase()) {
            case "web":
                return ClientType.WEB;
            case "android":
                return ClientType.ANDROID;
            case "ios":
                return ClientType.IOS;
            case "webservice":
                return ClientType.WEB_SERVICE;
            default:
                return null;
        }
    }

    public Integer getValue() {
        return value;
    }
}
