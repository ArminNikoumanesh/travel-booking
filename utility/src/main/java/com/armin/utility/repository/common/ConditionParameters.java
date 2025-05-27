package com.armin.utility.repository.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 **/
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
public class ConditionParameters {
    private String key;
    private Object value;

    public ConditionParameters(String key, Object value) {
        this.key = key;
        this.value = value;
    }
}
