package com.books.user.account.dto;

import com.books.utility.commons.interfaces.IValidation;
import com.books.utility.support.utils.NormalizeEngine;
import com.books.utility.system.exception.SystemException;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendOtpIn implements IValidation {
    @NotNull
    private String phoneNumber;

    public void setPhoneNumber(String phoneNumber) throws SystemException {
        this.phoneNumber = NormalizeEngine.getNormalizedPhoneNumber(phoneNumber);
    }

    @Override
    public void validate() throws SystemException {
        if (this.phoneNumber != null) {
            setPhoneNumber(this.phoneNumber);
        }
    }
}
