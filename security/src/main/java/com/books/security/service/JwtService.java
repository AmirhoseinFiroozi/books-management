package com.books.security.service;

import com.books.security.dto.JwtClaim;
import com.books.security.dto.SecurityAccessRule;
import com.books.security.dto.TokenInfo;
import com.books.security.repository.RestBaseDao;
import com.books.security.statics.constants.SecurityConstant;
import com.books.utility.commons.dto.UserContextDto;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The Json Web Token (JWT) Service Class,
 * Containing Methods about JWt Management
 *
 * @author Bijan Ghahremani
 * @version 1.0
 * @since 2016-09-22
 */

/**
 * Exceptions error code range: 2081-2100
 */
@Service
public class JwtService {
    private static final String CLAIM_USER_ID = "uid";
    private static final String CLAIM_SESSION_ID = "sid";
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 86400L;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 129600L;

    private final RestBaseDao restBaseDao;
    private final AvailableService availableService;
    private final SecretKey key;

    @Autowired
    public JwtService(RestBaseDao restBaseDao, AvailableService availableService) {
        this.restBaseDao = restBaseDao;
        this.availableService = availableService;
        this.key = Keys.hmacShaKeyFor("v8y/B?E(H+MbPeShVmYq3t6w9z$C&F)J@NcRfTjWnZr4u7x!A%D*G-KaPdSgVkXp".getBytes(StandardCharsets.UTF_8));
    }

    /* ****************************************************************************************************************** */

    public static JwtClaim validateClaims(Claims claims) throws SystemException {
        int claimUserId;
        int claimSessionId;
        try {
            claimUserId = Integer.parseInt(claims.get(JwtService.CLAIM_USER_ID).toString());
            claimSessionId = Integer.parseInt(claims.get(JwtService.CLAIM_SESSION_ID).toString());
        } catch (NumberFormatException e) {
            throw new SystemException(SystemError.TOKEN_VERIFICATION_FAILED, "claims:" + claims, 2090);
        } catch (NullPointerException | ClassCastException e) {
            throw new SystemException(SystemError.TOKEN_VERIFICATION_FAILED, "claims:" + claims, 2091);
        }
        return new JwtClaim(claimUserId, claimSessionId);
    }

    public TokenInfo create(Integer userId, Integer sessionId) throws SystemException {
        ClaimsBuilder claimsBuilder = Jwts.claims();
        claimsBuilder.add(JwtService.CLAIM_USER_ID, userId);
        claimsBuilder.add(JwtService.CLAIM_SESSION_ID, sessionId);
        Claims claims = claimsBuilder.build();

        String access = this.buildToken(claims, SecurityConstant.ACCESS_TOKEN_SUBJECT, ACCESS_TOKEN_EXPIRATION_TIME);
        String refresh = this.buildToken(claims, SecurityConstant.REFRESH_TOKEN_SUBJECT, REFRESH_TOKEN_EXPIRATION_TIME);
        return new TokenInfo(access, refresh, ACCESS_TOKEN_EXPIRATION_TIME, REFRESH_TOKEN_EXPIRATION_TIME);
    }

    public TokenInfo refresh(String token) throws SystemException {
        Claims claims = this.extractClaims(token, SecurityConstant.REFRESH_TOKEN_SUBJECT);

        String access = this.buildToken(claims, SecurityConstant.ACCESS_TOKEN_SUBJECT, ACCESS_TOKEN_EXPIRATION_TIME);
        String refresh = this.buildToken(claims, SecurityConstant.REFRESH_TOKEN_SUBJECT, REFRESH_TOKEN_EXPIRATION_TIME);
        return new TokenInfo(access, refresh, ACCESS_TOKEN_EXPIRATION_TIME, REFRESH_TOKEN_EXPIRATION_TIME);
    }

    public UserContextDto extractConfirmedUserFromToken(String token, String tokenType) throws SystemException {
        Claims claims = this.extractClaims(token, tokenType);
        return this.availableService.getConfirmedUserByAuthentication(claims);
    }

    public Integer extractSessionIdFromRefreshToken(String token) throws SystemException {
        Claims claims = this.extractClaims(token, SecurityConstant.REFRESH_TOKEN_SUBJECT);
        return this.availableService.getSessionId(claims);
    }

    public Integer extractSessionIdFromAccessToken(String token) throws SystemException {
        Claims claims = this.extractClaims(token, SecurityConstant.ACCESS_TOKEN_SUBJECT);
        return this.availableService.getSessionId(claims);
    }

    /* ****************************************************************************************************************** */

    public List<SecurityAccessRule> generateAccessRule() throws SystemException {
        List<SecurityAccessRule> accessRules = this.restBaseDao.listRestsWithPermissions();
        if (accessRules.isEmpty())
            throw new SystemException(SystemError.DATA_NOT_FOUND, "restEntities", 2081);
        return accessRules;
    }

    /**
     * Build a Token Based on Requested Claims
     *
     * @param claims            A {@link Map} Instance Representing Claims of Json Web Token
     * @param subject           A {@link String} Instance Representing Subject of Json Web Token
     * @param expirationMinutes An {@link int} Instance Representing Expiration Hours of Json Web Token
     * @return A {@link String} Instance Representing Requested Token
     * @throws SystemException A Customized {@link RuntimeException} with type of {@link SystemError#TOKEN_CREATION_FAILED} when Building Token Failed
     */
    private String buildToken(Claims claims, String subject, Long expirationMinutes) throws SystemException {
        try {
            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            Timestamp expiration = Timestamp.valueOf(LocalDateTime.now().plusMinutes(expirationMinutes));
            return Jwts.builder()
                    .setHeaderParam("typ", "JWT")
                    .setClaims(claims)
                    .setIssuer(SecurityConstant.TOKEN_ISSUER)
                    .setSubject(subject)
                    .setExpiration(expiration)
                    .setIssuedAt(now)
                    .setId(UUID.randomUUID().toString())
                    .compressWith(CompressionCodecs.GZIP)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
        } catch (RuntimeException e) {
            throw new SystemException(SystemError.TOKEN_CREATION_FAILED, "claims:" + claims + ",subject:" + subject, 2083);
        }
    }

    private Claims extractClaims(String token, String subject) throws SystemException {
        try {
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        } catch (MissingClaimException | IncorrectClaimException e) {
            throw new SystemException(SystemError.TOKEN_VERIFICATION_INVALID_TYPE, "token:" + token + ",subject:" + subject, 2086);
        } catch (ExpiredJwtException e) {
            if (subject.equals(SecurityConstant.REFRESH_TOKEN_SUBJECT)) {
                throw new SystemException(SystemError.REFRESH_TOKEN_VERIFICATION_EXPIRED, "subject:" + subject, 2087);
            } else {
                throw new SystemException(SystemError.TOKEN_VERIFICATION_EXPIRED, "subject:" + subject, 2088);
            }
        } catch (RuntimeException e) {
            throw new SystemException(SystemError.TOKEN_VERIFICATION_FAILED, "token:" + token + ",subject:" + subject, 2089);
        }
    }
}
