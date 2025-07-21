package com.books.security.dto;

import com.books.utility.commons.dto.UserContextDto;
import com.books.security.statics.constants.SecurityConstant;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * All Request turns into JwtOutputAuthentication Object
 * after Successful Authentication
 *
 * @author Omid Sohrabi
 * @version 2.0
 * @since 2016-09-22
 */
public class JwtOutputAuthentication implements Authentication {

    private final UserContextDto userContextDto;

    public JwtOutputAuthentication(UserContextDto user) {
        this.userContextDto = user;
    }

    @Override
    public Object getPrincipal() {
        return this.userContextDto;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userContextDto.getPermissionIds().stream()
                .map(eachId -> new SimpleGrantedAuthority(SecurityConstant.SIMPLE_GRANTED_AUTHORITY_PREFIX + eachId))
                .collect(Collectors.toSet());
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return null;
    }
}
