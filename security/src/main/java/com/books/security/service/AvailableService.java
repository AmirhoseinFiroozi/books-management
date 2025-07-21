package com.books.security.service;

import com.books.security.dto.JwtClaim;
import com.books.security.repository.UserSessionBaseDao;
import com.books.utility.commons.dto.UserContextDto;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Exceptions error code range: 2051-2070
 */
@Service
public class AvailableService {
    private final UserSessionBaseDao userSessionBaseDao;

    @Autowired
    public AvailableService(UserSessionBaseDao userSessionBaseDao) {
        this.userSessionBaseDao = userSessionBaseDao;
    }

    public UserContextDto getConfirmedUserByAuthentication(Claims claims) throws SystemException {
        JwtClaim jwtClaim = JwtService.validateClaims(claims);
        UserContextDto sessionWithUser = this.userSessionBaseDao.getSessionWithUser(jwtClaim.getSessionId());
        if (sessionWithUser == null) {
            throw new SystemException(SystemError.SESSION_EXPIRED, "sessionId", 1010);
        }
        if (sessionWithUser.getId() == null) {
            throw new SystemException(SystemError.USER_NOT_FOUND, "user", 2055);
        }
        if (sessionWithUser.isSuspended()) {
            throw new SystemException(SystemError.USER_NOT_ACTIVE, "userId:" + sessionWithUser.getId(), 2056);
        }
        if (sessionWithUser.getLockExpired() != null && sessionWithUser.getLockExpired().isAfter(LocalDateTime.now())) {
            throw new SystemException(SystemError.USER_NOT_ACTIVE, "userId:" + sessionWithUser.getId(), 3002);
        }
        return sessionWithUser;
    }

    /* ************************************************************************************************************** */

}
