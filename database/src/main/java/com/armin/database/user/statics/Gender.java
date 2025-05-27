package com.armin.database.user.statics;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
public enum Gender {
    MALE(0, "مرد"),
    FEMALE(1, "زن");

    int value;
    String text;

    Gender(int value) {
        this.value = value;
    }

    Gender(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    private static final Map<String, Gender> genderMap = new HashMap<String, Gender>();

    static {
        for (Gender g : Gender.values()) {
            genderMap.put(g.text,g);
        }
    }

    public static Gender getByText(String abbreviation) {
        return genderMap.get(abbreviation);
    }
}
