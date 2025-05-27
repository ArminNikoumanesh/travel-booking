package com.armin.operation.admin.statics.constants;

public abstract class UserRestApi {

    // UserController
    public static final String USERS_COUNT = "/users/count";
    public static final String USERS = "/users";
    public static final String USERS_ID = "/users/{id}";
    public static final String USERS_ID_ADMIN_ROLE = "/users/{id}/admin-role";
    public static final String USERS_ID_ROLE_REALM = "/users/{id}/role";
    public static final String USERS_ID_ENABLE_TWO_FACTOR = "/users/{id}/enable-two-factor-login";
    public static final String USERS_ID_DISABLE_TWO_FACTOR = "/users/{id}/disable-two-factor-login";
    public static final String USERS_ID_SUSPEND = "/users/{id}/suspend";
    public static final String USERS_ID_UNSUSPEND = "/users/{id}/unsuspend";
    public static final String USERS_ID_LOCK = "/users/{id}/lock";
    public static final String USERS_INFO = "/users/info";
    public static final String USERS_ID_CONFIRM_MOBILE = "/users/{id}/confirm-mobile";
    public static final String USERS_ID_CONFIRM_EMAIL = "/users/{id}/confirm-email";
    public static final String USERS_BANK_INFO = "/users/bank-info/{userId}";
    public static final String USERS_ID_INFO = "/users/{userId}/info";
    public static final String USERS_ID_UNLOCK = "/users/{id}/unlock";

}
