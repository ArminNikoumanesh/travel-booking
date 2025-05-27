package com.armin.utility.repository.orm;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
public abstract class Parameterized<T> {

    private Class<T> clazz;

    public Parameterized() {
        Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        clazz = type instanceof Class ? (Class<T>) type : null;
    }

    protected Class<T> getClazz() {
        return clazz;
    }
}
