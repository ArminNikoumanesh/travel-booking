---------------------- Security Rest ----------------------
--- Id Range 150000
---------------------- Security Permission ----------------------
--- Id Range 250000

-- Cloud Config Rest 
-- Id Range 150001 - 150999
INSERT
INTO SECURITY_REST(ID_PK, URL, HTTP_METHOD)
VALUES (150001, '/admin/cloud/config-settings/account', 'GET'),
       (150002, '/admin/cloud/config-settings/account', 'PUT'),
       (150003, '/admin/cloud/config-settings/security', 'GET'),
       (150004, '/admin/cloud/config-settings/security', 'PUT'),
       (150005, '/admin/cloud/config-settings/all', 'GET'),
       (150006, '/admin/cloud/config-settings/refresh', 'GET');

-- Cloud Config Permission
-- Id Range 250001 - 250999
INSERT
INTO SECURITY_PERMISSION(ID_PK, PARENT_ID_FK, NODE_TYPE, TRAVERSAL, NAME, TYPE)
VALUES (250001, null, 1, true, 'cloud-config', 'ADMIN'),
       (250002, 250001, 10, true, 'cloud-config.account', 'ADMIN'),
       (250003, 250001, 10, true, 'cloud-config.security', 'ADMIN');

INSERT
INTO SECURITY_PERMISSION_REST(PERMISSION_ID_FK, REST_ID_FK)
VALUES (250002, 150001),
       (250002, 150002),
       (250003, 150003),
       (250003, 150004),
       (250001, 150005),
       (250001, 150006);
