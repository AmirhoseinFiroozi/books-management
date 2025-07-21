package com.books.security.authentication.filter;

import com.books.utility.commons.dto.UserContextDto;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class JwtUser {

    public static UserContextDto getAuthenticatedUser() throws SystemException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserContextDto) {
            return (UserContextDto) authentication.getPrincipal();
        }
        throw new SystemException(SystemError.TOKEN_VERIFICATION_EXPIRED, "authentication", 2233);
    }

    public static Optional<UserContextDto> getPublicUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserContextDto) {
            return Optional.of((UserContextDto) authentication.getPrincipal());
        }
        return Optional.empty();
    }
}
