package com.books.application.security.realm.dto;

import com.books.application.security.realm.entity.SecurityRealmEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RealmOut {
    int id;
    String name;

    public RealmOut(SecurityRealmEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }
}
