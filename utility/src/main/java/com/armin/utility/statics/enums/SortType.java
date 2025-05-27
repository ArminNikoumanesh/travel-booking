package com.armin.utility.statics.enums;

public enum SortType {

    ASCENDING(true),
    DESCENDING(false);

    private final Boolean value;

    SortType(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }
}
