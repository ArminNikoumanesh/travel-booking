package com.armin.security.statics.enums;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
public enum RealmType {

    STORE(0),
    WARE_HOUSE(1),
    INVENTORY(1);

    int value;

    RealmType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }}


