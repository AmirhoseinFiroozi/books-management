package com.books.application.user.user.dto;

import com.books.utility.system.exception.SystemException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.books.utility.support.NormalizeEngine.getNormalizedPhoneNumber;

@Getter
@Setter
public class UserIn {
    @NotBlank
    private String username;
    @NotBlank
    private String phoneNumber;
    @Email
    private String email;
    private LocalDateTime lockExpired;
    private int accessFailedCount;
    private boolean suspended;

    private void setPhoneNumber(String phoneNumber) throws SystemException {
        this.phoneNumber = getNormalizedPhoneNumber(phoneNumber);
    }
}
