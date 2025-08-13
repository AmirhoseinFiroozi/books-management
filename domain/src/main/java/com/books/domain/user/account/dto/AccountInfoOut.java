package com.books.domain.user.account.dto;

import com.books.domain.user.user.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountInfoOut {
    private int id;
    private String username;
    private String phoneNumber;
    private String email;
    private boolean hasPassword;

    public AccountInfoOut(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.username = userEntity.getUsername();
        this.phoneNumber = userEntity.getPhoneNumber();
        this.email = userEntity.getEmail();
        this.hasPassword = userEntity.getHashedPassword() != null && !userEntity.getHashedPassword().isEmpty();
    }
}
