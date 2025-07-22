package com.books.user.account.dto;

import com.books.user.user.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserImageOut {
    private String image;

    public UserImageOut(UserEntity userEntity) {
        this.image = userEntity.getImage();
    }
}