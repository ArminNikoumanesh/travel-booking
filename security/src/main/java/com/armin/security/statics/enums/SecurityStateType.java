package com.armin.security.statics.enums;

public enum SecurityStateType {
    ADD(11),
    DELETE(12),
    EDIT(13),

    ROLE_DELETE(21),
    REALM_DELETE(22);

    private final int value;

    SecurityStateType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
