package com.armin.utility.object;

import org.apache.commons.lang3.builder.Diffable;

public interface IDiffLog<T> extends Diffable<T> {

    public DiffLogResult<T> getDifferentialLog(T diffEntity);

}
