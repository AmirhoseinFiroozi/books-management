package com.books.security.authentication.filter;

import com.books.security.dto.JwtOutputAuthentication;
import com.books.security.service.HeaderService;
import com.books.security.service.JwtService;
import com.books.security.statics.constants.SecurityConstant;
import com.books.utility.commons.dto.UserContextDto;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class IdentifiableAnonymousAuthenticationFilter extends AnonymousAuthenticationFilter {
    private final JwtService jwtService;
    private final HeaderService headerService;

    public IdentifiableAnonymousAuthenticationFilter(JwtService jwtService, HeaderService headerService) {
        super("identified.key");
        this.jwtService = jwtService;
        this.headerService = headerService;
    }

    @Override
    protected Authentication createAuthentication(HttpServletRequest request) {
        try {
            if (request == null)
                throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "request", 2028);

            String authToken = this.headerService.extractPublicTokenClient(request);
            if (authToken == null)
                return new JwtOutputAuthentication(null);
            try {
                UserContextDto userContextDto = this.jwtService.extractConfirmedUserFromToken(authToken, SecurityConstant.ACCESS_TOKEN_SUBJECT);
                return new JwtOutputAuthentication(userContextDto);
            } catch (SystemException e) {
                UserContextDto userContextDto = this.jwtService.extractConfirmedUserFromToken(authToken, SecurityConstant.REFRESH_TOKEN_SUBJECT);
                return new JwtOutputAuthentication(userContextDto);
            }
        } catch (SystemException e) {
            if (request != null) {
                request.setAttribute("token-expire", true);
            }
        }
        return new JwtOutputAuthentication(null);
    }
}
