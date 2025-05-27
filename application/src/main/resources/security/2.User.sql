INSERT
INTO SECURITY_REST(ID_PK, URL, HTTP_METHOD)
VALUES (120001, '/admin/users/count{([\?].*)?}', 'GET'),
       (120002, '/admin/users{([\?].*)?}', 'GET'),
       (120003, '/admin/users/{\\d+([\?].*)?}', 'GET'),
       (120004, '/admin/users', 'POST'),
       (120005, '/admin/users/{\\d+([\?].*)?}', 'PUT'),
       (120006, '/admin/users/{\\d+}', 'DELETE'),
       (120007, '/admin/users/{\\d+}/enable-two-factor-login', 'PATCH'),
       (120008, '/admin/users/{\\d+}/disable-two-factor-login', 'PATCH'),
       (120009, '/admin/users/{\\d+}/unlock', 'PATCH'),
       (120010, '/admin/users/{\\d+}/lock', 'PATCH'),
       (120011, '/admin/users/{\\d+}/role', 'PATCH'),
       (120012, '/admin/users/{\\d+}/admin-role', 'GET'),
       (120013, '/admin/users/{\\d+}/suspend', 'PATCH'),
       (120014, '/admin/users/{\\d+}/unsuspend', 'PATCH'),
       (120015, '/admin/users/{\\d+}/confirm-email', 'PATCH'),
       (120016, '/admin/users/{\\d+}/confirm-mobile', 'PATCH'),
       (120017, '/admin/users/{\\d+}/confirm-mobile-email', 'PATCH');

-- Security User Permission
-- Id Range 220000 - 220999
INSERT
INTO SECURITY_PERMISSION(ID_PK, PARENT_ID_FK, NODE_TYPE, TRAVERSAL, NAME, TYPE)
VALUES (220001, null, 1, true, 'user-management', 'ADMIN'),
       (220002, 220001, 10, true, 'user-management.users', 'ADMIN'),
       (220003, 220001, 10, true, 'user-management.users.create', 'ADMIN'),
       (220004, 220001, 10, true, 'user-management.users.edit', 'ADMIN'),
       (220005, 220001, 10, true, 'user-management.users.delete', 'ADMIN'),
       (220006, 220001, 10, true, 'user-management.users.two-factor', 'ADMIN'),
       (220007, 220001, 10, true, 'user-management.users.lock', 'ADMIN'),
       (220008, 220001, 10, true, 'user-management.users.roles', 'ADMIN'),
       (220009, 220001, 10, true, 'user-management.users.suspend', 'ADMIN'),
       (220010, 220001, 10, true, 'user-management.users.confirm-email', 'ADMIN'),
       (220011, 220001, 10, true, 'user-management.users.confirm-mobile', 'ADMIN'),
       (220012, 220011, 10, true, 'user-management.users.confirm-mobile-email', 'ADMIN');


INSERT
INTO SECURITY_PERMISSION_REST(PERMISSION_ID_FK, REST_ID_FK)
VALUES (220002, 120001),
       (220002, 120002),
       (220002, 120003),
       (220003, 120004),
       (220004, 120005),
       (220005, 120006),
       (220006, 120007),
       (220006, 120008),
       (220007, 120009),
       (220007, 120010),
       (220008, 120011),
       (220008, 120012),
       (220009, 120013),
       (220009, 120014),
       (220010, 120015),
       (220011, 120016),
       (220012, 120017);

--------------------------------------------------------------------------------------------------------------------------------------------
-- Security User Session Rest
-- Id Range 121000 - 121999
INSERT
INTO SECURITY_REST(ID_PK, URL, HTTP_METHOD)
VALUES (121001, '/admin/users/sessions/count{([\?].*)?}', 'GET'),
       (121002, '/admin/users/sessions{([\?].*)?}', 'GET'),
       (121003, '/admin/users/sessions/{\\d+([\?].*)?}', 'GET'),
       (121004, '/admin/users/sessions/{\\d+}', 'DELETE');

-- Security User Permission
-- Id Range 221000 - 221999
INSERT
INTO SECURITY_PERMISSION(ID_PK, PARENT_ID_FK, NODE_TYPE, TRAVERSAL, NAME, TYPE)
VALUES (221001, 210001, 10, true, 'identity.userSessions', 'ADMIN');

INSERT
INTO SECURITY_PERMISSION_REST(PERMISSION_ID_FK, REST_ID_FK)
VALUES (221001, 121001),
       (221001, 121002),
       (221001, 121003),
       (221001, 121004);
