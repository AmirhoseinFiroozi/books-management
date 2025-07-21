package com.books.security.statics.constants;

/**
 * Security Constants,
 * All Security Constants are Declared in SecurityConstant Class
 *
 * @author Omid Sohrabi
 * @version 1.0
 * @since 2019-04-27
 */
public abstract class SecurityConstant {
    public static final String HEADER_TOKEN_KEY = "Authorization";

    public static final String TOKEN_ISSUER = "com.books";

    public static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    public static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";

    public static final String REQUEST_EXTENDED_ATTRIBUTE = "user";

    public static final String ACCESS_RULE_PREFIX = "R";
    public static final String SIMPLE_GRANTED_AUTHORITY_PREFIX = "ROLE_" + SecurityConstant.ACCESS_RULE_PREFIX;

}
