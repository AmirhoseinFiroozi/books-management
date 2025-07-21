package com.books.security.authentication.filter;

import com.books.utility.system.exception.SystemException;
import org.springframework.security.core.AuthenticationException;

/**
 * Authentication Exception Class,
 * A Customized Authentication Exception for Spring Security
 *
 * @author Bijan Ghahremani
 * @version 1.0
 * @since 2016-09-22
 */
public class JwtAuthenticationException extends AuthenticationException {
    private final SystemException systemException;

    public JwtAuthenticationException(SystemException systemException) {
        super("", systemException);
        this.systemException = systemException;
    }

    public SystemException getSystemException() {
        return systemException;
    }
}
