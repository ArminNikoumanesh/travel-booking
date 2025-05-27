package com.armin.security.statics.constants;

public abstract class SecurityRestApi {

    // ManageSecurityController
    public static final String SECURITY_MANAGE_PERMISSION_LIST = "/identity/manage/permission/list";
    public static final String SECURITY_MANAGE_PERMISSION_GET = "/identity/manage/permission/getPermission";
    public static final String SECURITY_MANAGE_PERMISSION_EDIT = "/identity/manage/permission/edit";

    public static final String SECURITY_MANAGE_ROLE_CATEGORY_LIST = "/identity/manage/roleCategory/list";
    public static final String SECURITY_MANAGE_ROLE_LIST = "/identity/manage/role/list";
    public static final String SECURITY_MANAGE_ROLE_GET = "/identity/manage/role/getPermission";
    public static final String SECURITY_MANAGE_ROLE_ADD = "/identity/manage/role/add";
    public static final String SECURITY_MANAGE_ROLE_EDIT = "/identity/manage/role/edit/";
    public static final String SECURITY_MANAGE_ROLE_DELETE = "/identity/manage/role/delete/{id}";

    public static final String SECURITY_MANAGE_REALM_ADD = "/identity/manage/realm/add";
    public static final String SECURITY_MANAGE_REALM_GET = "/identity/manage/realm/get";
    public static final String SECURITY_MANAGE_REALM_EDIT = "/identity/manage/realm/edit";
    public static final String SECURITY_MANAGE_REALM_DELETE = "/identity/manage/realm/delete";

    // SecurityReportController
    public static final String SECURITY_REPORT_ROLE = "/identity/report/role";

    //SecurityEndpointController
    public static final String SECURITY_ENDPOINTS_COUNT = "/endpoints/count";
    public static final String SECURITY_ENDPOINTS = "/endpoints";
    public static final String SECURITY_ENDPOINTS_ID = "/endpoints/{id}";

    //SecurityPermissionController
    public static final String SECURITY_PERMISSIONS_COUNT = "/permissions/count";
    public static final String SECURITY_PERMISSIONS = "/permissions";
    public static final String SECURITY_PERMISSIONS_INFO = "/permissions/info";
    public static final String SECURITY_PERMISSIONS_ID = "/permissions/{id}";
    public static final String SECURITY_PERMISSIONS_COUNT_SELLER = "/permissions/seller/count";
    public static final String SECURITY_PERMISSIONS_SELLER = "/permissions/seller";
    public static final String SECURITY_PERMISSIONS_INFO_SELLER = "/permissions/seller/info";


    //SecurityRealmController
    public static final String SECURITY_REALMS_COUNT = "/realms/count";
    public static final String SECURITY_REALMS = "/realms";
    public static final String SECURITY_REALMS_ID = "/realms/{id}";
    public static final String SECURITY_REALMS_INFO = "/realms/info";


    //SecurityRoleController
    public static final String SECURITY_ROLES_COUNT = "/roles/count";
    public static final String SECURITY_ROLES_SELLER_COUNT = "/roles/sellers/count";
    public static final String SECURITY_ROLES = "/roles";
    public static final String SECURITY_ROLES_SELLERS = "/roles/sellers";
    public static final String SECURITY_ROLES_ID = "/roles/{id}";
    public static final String SECURITY_ROLES_SELLERS_ID = "/roles/sellers/{id}";
    public static final String SECURITY_ROLES_INFO = "/roles/info";


    //USER &  ROLE Controller
    public static final String SECURITY_ROLE_USER_ID = "/users/{userId}/roles";

    //SecurityRoleController
    public static final String SECURITY_ID_SESSION_COUNT = "/users/sessions/count";
    public static final String SECURITY_ID_SESSIONS = "/users/sessions";
    public static final String SECURITY_SESSION_BY_ID = "/users/sessions/{id}";
    public static final String SECURITY_SESSION_MY_COUNT = "/users/sessions/my/count";
    public static final String SECURITY_SESSION_MY = "/users/sessions/my";
    public static final String SECURITY_SESSION_MY_ID = "/users/sessions/my/{id}";

}
