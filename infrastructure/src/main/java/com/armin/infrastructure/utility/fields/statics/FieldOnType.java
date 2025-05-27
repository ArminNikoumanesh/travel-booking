package com.armin.infrastructure.utility.fields.statics;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
public enum  FieldOnType {
    BUSINESS(0),
    PLACE(1),
    CUSTOMER(2);
    private Integer value;

    FieldOnType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}
