package com.books.security.authentication.filter;

import com.books.security.dto.JwtInputAuthentication;
import com.books.security.dto.JwtOutputAuthentication;
import com.books.security.service.JwtService;
import com.books.utility.commons.dto.UserContextDto;
import com.books.security.statics.constants.SecurityConstant;
import com.books.utility.system.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * Authentication Provider Class,
 * Process Authentication in Spring Security
 *
 * @author Bijan Ghahremani
 * @version 1.0
 * @since 2016-09-22
 */
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtService jwtService;

    @Autowired
    public JwtAuthenticationProvider(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            UserContextDto userContextDto = this.jwtService.extractConfirmedUserFromToken((String) authentication.getPrincipal(), SecurityConstant.ACCESS_TOKEN_SUBJECT);
            return new JwtOutputAuthentication(userContextDto);
        } catch (SystemException e) {
            throw new JwtAuthenticationException(e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtInputAuthentication.class.equals(authentication);
    }
}
