package com.armin.security.statics.constants;

/**
 * Security Constants,
 * All Security Constants are Declared in SecurityConstant Class
 *
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
public abstract class SecurityConstant {
    public static final String HEADER_TOKEN_KEY = "Authorization";
    public static final String HEADER_CLIENT_SERIAL_NUMBER_KEY = "CSN";
    public static final String HEADER_CLIENT_TYPE_KEY = "CTY";

    public static final String TOKEN_ISSUER = "com.armin";

    public static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    public static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";

    public static final String REQUEST_EXTENDED_ATTRIBUTE = "user";

    public static final String ACCESS_RULE_PREFIX = "R";
    public static final String SIMPLE_GRANTED_AUTHORITY_PREFIX = "ROLE_" + SecurityConstant.ACCESS_RULE_PREFIX;

}
