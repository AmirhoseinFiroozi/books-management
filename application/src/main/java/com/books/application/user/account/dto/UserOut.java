package com.books.application.user.account.dto;

import com.books.application.user.user.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOut {
    private int id;
    private String fullName;
    private String phoneNumber;
    private String email;

    public UserOut(UserEntity entity) {
        this.id = entity.getId();
        this.fullName = entity.getFullName();
        this.phoneNumber = entity.getPhoneNumber();
        this.email = entity.getEmail();
    }
}