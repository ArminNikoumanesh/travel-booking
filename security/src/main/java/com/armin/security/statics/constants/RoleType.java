package com.armin.security.statics.constants;

import lombok.Getter;

@Getter
public enum RoleType {
    ADMIN(0),
    SELLER(1),
    DRIVER(2);

    public final int number;

    private RoleType(int number) {
        this.number = number;
    }
}
