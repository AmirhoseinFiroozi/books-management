package com.books.security.statics;

import lombok.Getter;

@Getter
public enum RoleCategoryType {
    SUPER_ADMIN(-1, "SUP_ADM", "مدیر اصلی");

    private final Integer code;
    private final String name;
    private final String title;

    RoleCategoryType(Integer code, String name, String title) {
        this.code = code;
        this.name = name;
        this.title = title;
    }

    }
