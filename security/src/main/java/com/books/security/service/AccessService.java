package com.books.security.service;

import com.books.security.statics.constants.SecurityConstant;
import com.books.utility.commons.dto.UserContextDto;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessService {

    private final HeaderService headerService;
    private final JwtService jwtService;

    @Autowired
    public AccessService(HeaderService headerService, JwtService jwtService) {
        this.headerService = headerService;
        this.jwtService = jwtService;
    }

    public String getAuthenticatedToken(HttpServletRequest request) throws SystemException {
        if (request == null)
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "request", 2025);

        return this.headerService.extractAuthTokenClient(request);
    }

    public UserContextDto getAuthenticatedUserFromRefreshToken(HttpServletRequest request) throws SystemException {
        if (request == null)
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "request", 2030);

        String authToken = this.headerService.extractAuthTokenClient(request);
        return this.jwtService.extractConfirmedUserFromToken(authToken, SecurityConstant.REFRESH_TOKEN_SUBJECT);
    }

    public Integer getSessionIdFromAccessToken(HttpServletRequest request) throws SystemException {
        if (request == null)
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "request", 2031);

        String authToken = this.headerService.extractAuthTokenClient(request);
        return this.jwtService.extractSessionIdFromAccessToken(authToken);
    }

    public Integer getSessionIdFromRefreshToken(HttpServletRequest request) throws SystemException {
        if (request == null)
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "request", 2032);

        String authToken = this.headerService.extractAuthTokenClient(request);
        return this.jwtService.extractSessionIdFromRefreshToken(authToken);
    }
}

