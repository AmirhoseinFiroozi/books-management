package com.books.application.user.account.dto;

import com.books.utility.commons.interfaces.IValidation;
import com.books.utility.system.exception.SystemException;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import static com.books.utility.support.NormalizeEngine.getNormalizedPhoneNumber;
import static com.books.utility.support.NormalizeEngine.normalizePersianString;


@Getter
@Setter
public class LoginIn implements IValidation {
    @NotNull
    private String phoneNumber;
    @NotNull
    private String password;

    public void setPhoneNumber(String phoneNumber) throws SystemException {
        this.phoneNumber = getNormalizedPhoneNumber(phoneNumber);
    }

    public void setPassword(String password) {
        this.password = normalizePersianString(password);
    }

    @Override
    public void validate() throws SystemException {
        if (this.phoneNumber != null) {
            setPhoneNumber(this.phoneNumber);
        }
        if (this.password != null) {
            setPassword(this.password);
        }
    }
}