package com.books.application.user.account.dto;

import com.books.utility.commons.interfaces.IValidation;
import com.books.utility.config.model.ApplicationProperties;
import com.books.utility.support.factory.StaticApplicationContext;
import com.books.utility.system.exception.ErrorResult;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.books.utility.support.NormalizeEngine.getNormalizedPhoneNumber;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterIn implements IValidation {
    @NotBlank
    private String username;
    @NotBlank
    private String phoneNumber;
    @Email
    private String email;
    private String password;

    public void setPhoneNumber(String phoneNumber) throws SystemException {
        this.phoneNumber = getNormalizedPhoneNumber(phoneNumber);
    }

    @Override
    public void validate() throws SystemException {
        ApplicationProperties applicationProperties = StaticApplicationContext.getContext().getBean(ApplicationProperties.class);
        List<ErrorResult> errorResults = new ArrayList<>();

        setPhoneNumber(this.phoneNumber);

        if (StringUtils.isBlank(this.password)) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "password should not be blank", 3468));
            errorResults.add(errorResult);
        }

        if (this.password.length() > applicationProperties.getIdentitySettings().getPassword().getMaxLength() ||
                this.password.length() < applicationProperties.getIdentitySettings().getPassword().getRequiredLength()) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "password length validation", 3461));
            errorResults.add(errorResult);
        }

        if (this.password.chars().distinct().count() < applicationProperties.getIdentitySettings().getPassword().getRequiredUniqueChars()) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "password unique character validation", 3462));
            errorResults.add(errorResult);
        }
        if (applicationProperties.getIdentitySettings().getPassword().isRequireDigit() && this.password.chars().noneMatch(Character::isDigit)) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "password digit validation", 3464));
            errorResults.add(errorResult);
        }

        if (applicationProperties.getIdentitySettings().getPassword().isRequireLowercase() && this.password.chars().noneMatch(Character::isLowerCase)) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "password lower case validation", 3465));
            errorResults.add(errorResult);
        }

        if (applicationProperties.getIdentitySettings().getPassword().isRequireUppercase() && this.password.chars().noneMatch(Character::isUpperCase)) {
            ErrorResult errorResult = new ErrorResult(new SystemException(SystemError.VALIDATION_EXCEPTION, "password upper case validation", 3466));
            errorResults.add(errorResult);
        }

        if (!errorResults.isEmpty()) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "multiple validation exception on password", 3467, errorResults);
        }
    }
}
