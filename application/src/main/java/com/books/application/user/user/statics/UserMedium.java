package com.books.application.user.user.statics;

import lombok.Getter;

@Getter
public enum UserMedium {
    WEB(0),
    ANDROID(1),
    IOS(2),
    WEB_SERVICE(3),
    ADMIN(4),
    UNKNOWN(5);
    private final Integer value;

    UserMedium(Integer value) {
        this.value = value;
    }
}
