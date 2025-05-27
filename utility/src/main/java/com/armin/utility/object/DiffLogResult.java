package com.armin.utility.object;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.DiffResult;

import java.util.Map;

@Getter
@Setter
public class DiffLogResult<T> {
    private Map<String, Object> statics;
    private DiffResult<T> details;

    public DiffLogResult(DiffResult<T> details) {
        this.details = details;
    }

    public DiffLogResult(Map<String, Object> statics, DiffResult<T> details) {
        this.statics = statics;
        this.details = details;
    }
}
