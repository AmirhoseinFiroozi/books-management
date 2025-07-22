package com.books.user.account.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserImageIn {
    @Size(max = 100)
    private String image;
}