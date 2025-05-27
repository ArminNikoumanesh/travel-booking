package com.armin.utility.file.statics.enums;

public enum FileServiceStatus {

    SUCCESS(true),
    FAILURE(false),
    ALREADY_EXISTS(false),
    WRONG_PARAMETER(false),
    NOT_EMPTY(false),
    ACCESS_DENIED(false);

    private final Boolean value;

    FileServiceStatus(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
