package com.books.domain.user.account.dto;

import com.books.utility.commons.interfaces.IValidation;
import com.books.utility.config.model.ApplicationProperties;
import com.books.utility.support.factory.StaticApplicationContext;
import com.books.utility.system.exception.ErrorResult;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.books.utility.support.NormalizeEngine.normalizePersianString;

@Getter
@Setter
@NoArgsConstructor
public class ChangePasswordIn implements IValidation {
    @NotNull
    private String newPassword;
    @NotNull
    private String newPasswordConfirm;

    public void setNewPassword(String newPassword) {
        this.newPassword = normalizePersianString(newPassword);
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = normalizePersianString(newPasswordConfirm);
    }

    @Override
    public void validate() throws SystemException {
        setNewPassword(this.newPassword);
        setNewPasswordConfirm(this.newPasswordConfirm);
        ApplicationProperties applicationProperties = StaticApplicationContext.getContext().getBean(ApplicationProperties.class);
        List<ErrorResult> errorResults = new ArrayList<>();

        if (StringUtils.isBlank(this.newPassword) || StringUtils.isBlank(this.newPasswordConfirm)) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "passwords shouldn't be null", 3470);
        }

        if (!Objects.equals(newPassword, newPasswordConfirm)) {
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "passwords do not match", 3471);
        }

        if (this.newPassword.length() > applicationProperties.getIdentitySettings().getPassword().getMaxLength() ||
                this.newPassword.length() < applicationProperties.getIdentitySettings().getPassword().getRequiredLength()) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "password length validation", 3472));
            errorResults.add(errorResult);
        }

        if (this.newPassword.chars().distinct().count() < applicationProperties.getIdentitySettings().getPassword().getRequiredUniqueChars()) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "password unique character validation", 3473));
            errorResults.add(errorResult);
        }
        if (applicationProperties.getIdentitySettings().getPassword().isRequireDigit() && this.newPassword.chars().noneMatch(Character::isDigit)) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "password digit validation", 3474));
            errorResults.add(errorResult);
        }

        if (applicationProperties.getIdentitySettings().getPassword().isRequireLowercase() && this.newPassword.chars().noneMatch(Character::isLowerCase)) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "password lower case validation", 3475));
            errorResults.add(errorResult);
        }

        if (applicationProperties.getIdentitySettings().getPassword().isRequireUppercase() && this.newPassword.chars().noneMatch(Character::isUpperCase)) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "password upper case validation", 3476));
            errorResults.add(errorResult);
        }

        if (!errorResults.isEmpty()) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "multiple validation exception on class UserRegistrationIn", 3477, errorResults);
        }
    }
}
