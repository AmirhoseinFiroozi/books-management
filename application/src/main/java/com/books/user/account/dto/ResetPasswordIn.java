package com.books.user.account.dto;

import com.books.utility.commons.interfaces.IValidation;
import com.books.utility.config.model.ApplicationProperties;
import com.books.utility.support.factory.StaticApplicationContext;
import com.books.utility.support.utils.NormalizeEngine;
import com.books.utility.system.exception.ErrorResult;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.books.utility.support.utils.NormalizeEngine.normalizePersianString;

@Getter
@Setter
public class ResetPasswordIn implements IValidation {
    @NotNull
    private String phoneNumber;
    @NotNull
    private String code;
    @Size(min = 6, max = 50)
    private String newPassword;
    @NotNull
    private String newPasswordConfirm;

    public void setPhoneNumber(String phoneNumber) throws SystemException {
        this.phoneNumber = NormalizeEngine.getNormalizedPhoneNumber(phoneNumber);
    }

    @Override
    public void validate() throws SystemException {
        if (this.code != null) {
            setCode(normalizePersianString(this.code));
        }
        if (this.phoneNumber != null) {
            setPhoneNumber(this.phoneNumber);
        }
        setNewPassword(normalizePersianString(this.newPassword));
        if (this.newPasswordConfirm != null) {
            setNewPasswordConfirm(normalizePersianString(this.newPasswordConfirm));
        }
        if (!Objects.equals(newPassword, newPasswordConfirm)) {
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "passwords do not match", 3448);
        }
        ApplicationProperties applicationProperties = StaticApplicationContext.getContext().getBean(ApplicationProperties.class);
        List<ErrorResult> errorResults = new ArrayList<>();

        if (this.newPassword.length() < applicationProperties.getIdentitySettings().getPassword().getRequiredLength()) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword length validation", 3441));
            errorResults.add(errorResult);
        }

        if (this.newPassword.chars().distinct().count() < applicationProperties.getIdentitySettings().getPassword().getRequiredUniqueChars()) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword unique character validation", 3442));
            errorResults.add(errorResult);
        }

        if (applicationProperties.getIdentitySettings().getPassword().isRequireNonAlphaNumeric() && this.newPassword.chars().allMatch(Character::isLetterOrDigit)) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword non alpha numeric validation", 3443));
            errorResults.add(errorResult);
        }

        if (applicationProperties.getIdentitySettings().getPassword().isRequireDigit() && this.newPassword.chars().noneMatch(Character::isDigit)) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword digit validation", 3444));
            errorResults.add(errorResult);
        }

        if (applicationProperties.getIdentitySettings().getPassword().isRequireLowercase() && this.newPassword.chars().noneMatch(Character::isLowerCase)) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword lower case validation", 3445));
            errorResults.add(errorResult);
        }

        if (applicationProperties.getIdentitySettings().getPassword().isRequireUppercase() && this.newPassword.chars().noneMatch(Character::isUpperCase)) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "newPassword upper case validation", 3446));
            errorResults.add(errorResult);
        }
        if (!errorResults.isEmpty()) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "multiple validation exception on class ResetPasswordIn", 3447, errorResults);
        }
    }
}

