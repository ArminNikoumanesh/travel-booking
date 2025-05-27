package com.armin.utility.repository.odm.object;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RangeConditionParameters {
    private String key;
    private Object from;
    private Object to;

    public RangeConditionParameters(String key, Object from, Object to) {
        this.key = key;
        this.from = from;
        this.to = to;
    }
}
