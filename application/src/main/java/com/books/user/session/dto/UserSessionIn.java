package com.books.user.session.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserSessionIn {
    @NotNull
    private Integer userId;
    @NotNull
    @NotNull
    private String os;
    @NotNull
    private String agent;
    private String firebaseToken;
    @NotNull
    private String ip;
}