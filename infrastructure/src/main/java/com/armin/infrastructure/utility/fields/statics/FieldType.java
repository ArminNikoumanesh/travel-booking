package com.armin.infrastructure.utility.fields.statics;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
public enum  FieldType {
    BOOLEAN(0),
    TEXT(1),
    LONG(2),
    DOUBLE(3),
    SELECT(4),
    MULTI_SELECT(5);
    private Integer value;

    FieldType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}
