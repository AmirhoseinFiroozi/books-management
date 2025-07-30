package com.books.application.user.user.statics;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum Gender {
    MALE(0, "مرد"),
    FEMALE(1, "زن");

    @Getter
    final int value;
    final String text;

    Gender(int value, String text) {
        this.value = value;
        this.text = text;
    }

    private static final Map<String, Gender> genderMap = new HashMap<>();

    static {
        for (Gender g : Gender.values()) {
            genderMap.put(g.text, g);
        }
    }

    public static Gender getByText(String abbreviation) {
        return genderMap.get(abbreviation);
    }
}