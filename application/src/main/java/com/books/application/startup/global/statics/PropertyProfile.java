package com.books.application.startup.global.statics;

/**
 * @auther : Armin.Nik
 * @project : shared
 * @date : 09.10.22
 */
public enum PropertyProfile {
    DEV("dev"),
    PROD("prod"),
    STAGE("stage");

    private String value;

    PropertyProfile(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
