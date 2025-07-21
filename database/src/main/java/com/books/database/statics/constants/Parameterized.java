package com.books.database.statics.constants;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author imax
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
