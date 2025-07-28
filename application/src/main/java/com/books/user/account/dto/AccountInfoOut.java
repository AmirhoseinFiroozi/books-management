package com.books.user.account.dto;

import com.books.user.user.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountInfoOut {
    private int id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private boolean hasPassword;

    public AccountInfoOut(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.fullName = userEntity.getFullName();
        this.phoneNumber = userEntity.getPhoneNumber();
        this.email = userEntity.getEmail();
        this.hasPassword = userEntity.getHashedPassword() != null && !userEntity.getHashedPassword().isEmpty();
    }
}
