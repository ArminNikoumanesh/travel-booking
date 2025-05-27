package com.armin.database.user.statics;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
public enum  LocaleType {
    en(0),
    fa(1),
    ar(2);


    int value;

    LocaleType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
