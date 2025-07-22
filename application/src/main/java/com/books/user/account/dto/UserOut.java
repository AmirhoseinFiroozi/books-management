package com.books.user.account.dto;

import com.books.user.user.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOut {
    private int id;
    private String fullName;
    private String displayName;
    private String image;
    private String phoneNumber;
    private String email;

    public UserOut(UserEntity entity) {
        this.id = entity.getId();
        this.fullName = entity.getFullName();
        this.displayName = entity.getDisplayName();
        this.image = entity.getImage();
        this.phoneNumber = entity.getPhoneNumber();
        this.email = entity.getEmail();
    }
}