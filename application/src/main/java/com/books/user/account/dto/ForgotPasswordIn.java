package com.books.user.account.dto;

import com.books.utility.commons.interfaces.IValidation;
import com.books.utility.support.utils.NormalizeEngine;
import com.books.utility.system.exception.SystemException;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordIn implements IValidation {
    @NotNull
    private String key;
    @NotNull
    private String code;

    public void setKey(String key) throws SystemException {
        this.key = NormalizeEngine.getNormalizedPhoneNumber(key);
    }

    public void setCode(String code) {
        this.code = NormalizeEngine.normalizePersianDigits(code);
    }

    @Override
    public void validate() throws SystemException {
        if (this.key != null) {
            setKey(this.key);
        }
        if (this.code != null) {
            setCode(this.code);
        }
    }
}
