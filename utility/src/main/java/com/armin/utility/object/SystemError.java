package com.armin.utility.object;

import jakarta.servlet.http.HttpServletResponse;

/**
 * System Error Enum,
 * All System Errors Types are Declared in SystemError Enum
 *
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
public enum SystemError {

    /**
     * General
     */
    SERVER_ERROR(HttpServletResponse.SC_SERVICE_UNAVAILABLE),
    REQUEST_EXPIRED(HttpServletResponse.SC_REQUEST_TIMEOUT),
    REQUIRED_FIELDS_NOT_SET(HttpServletResponse.SC_EXPECTATION_FAILED),

    IO_EXCEPTION(HttpServletResponse.SC_SERVICE_UNAVAILABLE),
    ILLEGAL_ARGUMENT(HttpServletResponse.SC_EXPECTATION_FAILED),
    INVALID_DATA_TYPE(HttpServletResponse.SC_EXPECTATION_FAILED),
    DATA_NOT_FOUND(HttpServletResponse.SC_NOT_FOUND),
    VALIDATION_EXCEPTION(HttpServletResponse.SC_BAD_REQUEST),

    /**
     * Utility
     */
    HASH_FUNCTION_FAILED(HttpServletResponse.SC_SERVICE_UNAVAILABLE),
    UPLOADED_FILE_CORRUPTED(HttpServletResponse.SC_BAD_REQUEST),
    STORE_FILE_FAILED(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE),
    EMAIL_ADDRESS_NOT_VALID(HttpServletResponse.SC_EXPECTATION_FAILED),
    EMAIL_SEND_FAILED(HttpServletResponse.SC_SERVICE_UNAVAILABLE),
    GOOGLE_API_EXCEPTION(HttpServletResponse.SC_SERVICE_UNAVAILABLE),
    GOOGLE_NOT_RESPONSE(HttpServletResponse.SC_SERVICE_UNAVAILABLE),

    /**
     * Database
     */
    STORED_PROCEDURE_FAILED(HttpServletResponse.SC_SERVICE_UNAVAILABLE),
    VIOLATION_ERROR(490),
    CHILD_RECORD_FOUND(491),

    /**
     * Security
     */
    ACCESS_DENIED(HttpServletResponse.SC_UNAUTHORIZED),
    LOGICAL_UN_AUTHORIZED(HttpServletResponse.SC_METHOD_NOT_ALLOWED),

    TOKEN_CREATION_FAILED(HttpServletResponse.SC_SERVICE_UNAVAILABLE),
    TOKEN_VERIFICATION_INVALID_TYPE(HttpServletResponse.SC_UNAUTHORIZED),
    TOKEN_VERIFICATION_EXPIRED(HttpServletResponse.SC_UNAUTHORIZED),
    TOKEN_VERIFICATION_FAILED(HttpServletResponse.SC_UNAUTHORIZED),
    REFRESH_TOKEN_VERIFICATION_EXPIRED(HttpServletResponse.SC_UNAUTHORIZED),

    INVALID_TOKEN_HEADER(HttpServletResponse.SC_NOT_ACCEPTABLE),
    INVALID_PUBLIC_TOKEN(425),
    INVALID_CLIENT_TYPE(HttpServletResponse.SC_NOT_ACCEPTABLE),

    INVALID_VALIDATION_CODE(HttpServletResponse.SC_EXPECTATION_FAILED),
    VALIDATION_CODE_EXPIRED(HttpServletResponse.SC_EXPECTATION_FAILED),
    INVALID_USER_TYPE(HttpServletResponse.SC_NOT_ACCEPTABLE),

    SESSION_EXPIRED(HttpServletResponse.SC_FORBIDDEN),

    USER_BLOCKED(HttpServletResponse.SC_UNAUTHORIZED),
    MOBILE_NOT_CONFIRM(HttpServletResponse.SC_PRECONDITION_FAILED),
    EMAIL_NOT_CONFIRM(444),
    USER_NOT_ACTIVE(HttpServletResponse.SC_UNAUTHORIZED),
    USER_NOT_LOGIN(HttpServletResponse.SC_UNAUTHORIZED),
    USER_NOT_FOUND(HttpServletResponse.SC_NOT_FOUND),

    /**
     * User
     */
    USERNAME_ALREADY_EXIST(HttpServletResponse.SC_EXPECTATION_FAILED),
    USERNAME_PASSWORD_NOT_MATCH(HttpServletResponse.SC_EXPECTATION_FAILED),

    FILE_NOT_FOUND(HttpServletResponse.SC_NOT_FOUND),
    READ_META_DATA(HttpServletResponse.SC_NOT_FOUND),

    /**
     * Elastic
     */
    ELASTIC_NOT_RESPOND(HttpServletResponse.SC_SERVICE_UNAVAILABLE),
    ELASTIC_CONNECTION(HttpServletResponse.SC_SERVICE_UNAVAILABLE),
    ELASTIC_INTERRUPTED(HttpServletResponse.SC_SERVICE_UNAVAILABLE),
    ELASTIC_EXECUTION_EXCEPTION(HttpServletResponse.SC_SERVICE_UNAVAILABLE),

    /**
     * Retail
     */
    DUPLICATE_REQUEST(HttpServletResponse.SC_EXPECTATION_FAILED),
    ILLEGAL_REQUEST(HttpServletResponse.SC_EXPECTATION_FAILED),
    UN_SUPPORTED_REQUEST(HttpServletResponse.SC_NOT_ACCEPTABLE),

    /**
     * SMS
     */

    SMS_NOT_SENT(HttpServletResponse.SC_EXPECTATION_FAILED),
    SMS_SERVICE_UNAVAILABLE(HttpServletResponse.SC_SERVICE_UNAVAILABLE),

    SERVICE_UNAVAILABLE(HttpServletResponse.SC_SERVICE_UNAVAILABLE),

    /**
     * File
     */
    DESTINATION_PATH_NOT_EXISTS(HttpServletResponse.SC_NOT_FOUND),
    DESTINATION_PATH_ALREADY_EXISTS(HttpServletResponse.SC_CONFLICT);

    private final Integer value;

    SystemError(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
