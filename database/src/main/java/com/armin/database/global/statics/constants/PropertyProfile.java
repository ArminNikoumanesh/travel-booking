package com.armin.database.global.statics.constants;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
public enum PropertyProfile {
    DEV("dev"),
    PROD("prod"),
    STAGE("stage");

    private String value;

    PropertyProfile(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
