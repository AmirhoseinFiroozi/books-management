package com.books.domain.user.user.dto;

import com.books.domain.user.user.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserInfoOut {
    private int id;
    private String username;
    private String phoneNumber;
    private String email;

    public UserInfoOut(UserEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.username = entity.getUsername();
            this.phoneNumber = entity.getPhoneNumber();
            this.email = entity.getEmail();
        }
    }
}
