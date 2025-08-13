package com.books.domain.user.account.dto;

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
    private String username;
    @NotNull
    private String password;

    public void setUsername(String username) throws SystemException {
        try {
            this.username = getNormalizedPhoneNumber(username);
        } catch (Exception e) {
            this.username = username;
        }
    }

    public void setPassword(String password) {
        this.password = normalizePersianString(password);
    }

    @Override
    public void validate() throws SystemException {
        if (this.username != null) {
            setUsername(this.username);
        }
        if (this.password != null) {
            setPassword(this.password);
        }
    }
}