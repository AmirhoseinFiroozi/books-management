package com.books.utility.support;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class ValidationEngine {
    public static <T> List<String> fieldNames(Class<T> cls) {
        List<String> names = new ArrayList<>();
        if (cls.getSuperclass() != null)
            names = fieldNames(cls.getSuperclass());
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields)
            names.add(field.getName());
        return names;
    }
}
