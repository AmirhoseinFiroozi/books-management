package com.books.application.startup.init.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ELPropertiesModel {
    private String id;
    private String name;
    private String profile;
    private Object property;
}
