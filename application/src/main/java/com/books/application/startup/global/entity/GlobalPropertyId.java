package com.books.application.startup.global.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * @auther : Armin.Nik
 * @project : shared
 * @date : 10.10.22
 */
@Getter
@Setter
@Embeddable
public class GlobalPropertyId implements Serializable {
    private String name;
    private String profile;

    public GlobalPropertyId(String name, String profile) {
        this.name = name;
        this.profile = profile;
    }

    public GlobalPropertyId() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GlobalPropertyId that = (GlobalPropertyId) o;
        return Objects.equals(name, that.name) && Objects.equals(profile, that.profile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, profile);
    }
}
