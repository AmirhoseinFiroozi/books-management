package com.books.startup.init.statics;

/**
 * @auther : Armin.Nik
 * @project : vrp-helm
 * @date : 04.10.22
 */
public enum PropertiesFetchMode {
    EL("pr-init-el"),
    DB("pr-init-db"),
    FILE("pr-init-file");


    private String value;

    PropertiesFetchMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
