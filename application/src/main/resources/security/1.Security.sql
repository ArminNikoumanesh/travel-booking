---------------------- Security Rest ----------------------
--- Id Range 110000
---------------------- Security Permission ----------------------
--- Id Range 210000
------------------------------------------------------------------------------------------------------------------------
-- Security Role Rest
-- Id Range 110000 - 110020
INSERT
INTO SECURITY_REST(ID_PK, URL, HTTP_METHOD)
VALUES (110001, '/admin/roles/count{([\?].*)?}', 'GET'),
       (110002, '/admin/roles{([\?].*)?}', 'GET'),
       (110003, '/admin/roles/{\\d+([\?].*)?}', 'GET'),
       (110004, '/admin/roles{([\?].*)?}', 'POST'),
       (110005, '/admin/roles/{\\d+}', 'PUT'),
       (110006, '/admin/roles/{\\d+}', 'DELETE');

-- Security Role Permission
-- Id Range 210000 - 210010
INSERT
INTO SECURITY_PERMISSION(ID_PK, PARENT_ID_FK, NODE_TYPE, TRAVERSAL, NAME, TYPE)
VALUES (210001, null, 1, true, ' identity', 'ADMIN'),
       (210002, 210001, 5, true, 'identity.roles', 'ADMIN'),
       (210003, 210002, 10, true, 'identity.roles.read', 'ADMIN'),
       (210004, 210002, 10, true, 'identity.roles.modify', 'ADMIN');


INSERT
INTO SECURITY_PERMISSION_REST(PERMISSION_ID_FK, REST_ID_FK)
VALUES (210003, 110001),
       (210003, 110002),
       (210003, 110003),
       (210004, 110004),
       (210004, 110005),
       (210004, 110006);
------------------------------------------------------------------------------------------------------------------------
-- Security Permission Rest
-- Id Range 110020 - 110040
INSERT
INTO SECURITY_REST(ID_PK, URL, HTTP_METHOD)
VALUES (110021, '/admin/permissions/count{([\?].*)?}', 'GET'),
       (110022, '/admin/permissions{([\?].*)?}', 'GET'),
       (110023, '/admin/permissions/{\\d+([\?].*)?}', 'GET'),
       (110024, '/admin/permissions{([\?].*)?}', 'POST'),
       (110025, '/admin/permissions/{\\d+}', 'PUT');

-- Security Permission Permission
-- Id Range 210010 - 210020
INSERT
INTO SECURITY_PERMISSION(ID_PK, PARENT_ID_FK, NODE_TYPE, TRAVERSAL, NAME, TYPE)
VALUES (210010, 210001, 5, true, 'identity.permissions', 'ADMIN'),
       (210012, 210010, 10, true, 'identity.permissions.read', 'ADMIN'),
       (210013, 210010, 10, true, 'identity.permissions.modify', 'ADMIN');


INSERT
INTO SECURITY_PERMISSION_REST(PERMISSION_ID_FK, REST_ID_FK)
VALUES (210012, 110021),
       (210012, 110022),
       (210012, 110023),
       (210013, 110024),
       (210013, 110025);

------------------------------------------------------------------------------------------------------------------------
-- Security Realm Rest
-- Id Range 110040 - 110060
INSERT
INTO SECURITY_REST(ID_PK, URL, HTTP_METHOD)
VALUES (110041, '/admin/realms/count{([\?].*)?}', 'GET'),
       (110042, '/admin/realms{([\?].*)?}', 'GET'),
       (110043, '/admin/realms/{\\d+([\?].*)?}', 'GET'),
       (110044, '/admin/realms{([\?].*)?}', 'POST'),
       (110045, '/admin/realms/{\\d+}', 'PUT'),
       (110046, '/admin/realms/{\\d+}', 'DELETE');

-- Security Realm Permission
-- Id Range 210020 - 210030
INSERT
INTO SECURITY_PERMISSION(ID_PK, PARENT_ID_FK, NODE_TYPE, TRAVERSAL, NAME, TYPE)
VALUES (210020, 210001, 5, true, 'identity.realms', 'ADMIN'),
       (210022, 210020, 10, true, 'identity.realms.read', 'ADMIN'),
       (210023, 210020, 10, true, 'identity.realms.modify', 'ADMIN');


INSERT
INTO SECURITY_PERMISSION_REST(PERMISSION_ID_FK, REST_ID_FK)
VALUES (210022, 110041),
       (210022, 110042),
       (210022, 110043),
       (210023, 110044),
       (210023, 110045),
       (210023, 110046);
------------------------------------------------------------------------------------------------------------------------
-- Handlebar template Rest
-- Id Range 110060 - 110080
INSERT
INTO SECURITY_REST (ID_PK, URL, HTTP_METHOD)
VALUES (110061, '/admin/handlebars-templates/count{([\?].*)?}', 'GET'),
       (110062, '/admin/handlebars-template/{\\d+}', 'GET'),
       (110063, '/admin/handlebars-templates{([\?].*)?}', 'GET'),
       (110064, '/admin/handlebars-template/{\\d+}', 'PUT');

-- Handlebar Permission
--Id Range 210030 -210040
INSERT
INTO SECURITY_PERMISSION(ID_PK, PARENT_ID_FK, NODE_TYPE, TRAVERSAL, NAME, TYPE)
VALUES (210031, null, 1, true, 'handlebars', 'ADMIN'),
       (210032, 210031, 10, true, 'handlebars.template', 'ADMIN'),
       (210033, 210031, 10, true, 'handlebars.template.modify', 'ADMIN');


INSERT
INTO SECURITY_PERMISSION_REST (PERMISSION_ID_FK, REST_ID_FK)
VALUES (210032, 110061),
       (210032, 110062),
       (210032, 110063),
       (210033, 110064);
------------------------------------------------------------------------------------------------------------------------

-- Security Endpoint Rest
-- Id Range 110080 - 110100
INSERT
INTO SECURITY_REST (ID_PK, URL, HTTP_METHOD)
VALUES (110081, '/admin/endpoints/count{([\?].*)?}', 'GET'),
       (110082, '/admin/endpoints/{\\d+}', 'GET'),
       (110083, '/admin/endpoints{([\?].*)?}', 'GET');

-- Endpoint Permission
--Id Range 210040 -210050
INSERT
INTO SECURITY_PERMISSION(ID_PK, PARENT_ID_FK, NODE_TYPE, TRAVERSAL, NAME, TYPE)
VALUES (210041, null, 1, true, 'endpoints', 'ADMIN'),
       (210042, 210041, 10, true, 'endpoints.read', 'ADMIN');


INSERT
INTO SECURITY_PERMISSION_REST (PERMISSION_ID_FK, REST_ID_FK)
VALUES (210042, 110081),
       (210042, 110082),
       (210042, 110083);
------------------------------------------------------------------------------------------------------------------------
-- Ticket  Detail
-- Id Range 110100 - 110120
INSERT
INTO SECURITY_REST(ID_PK, URL, HTTP_METHOD)
VALUES (110101, '/admin/tickets/user/count{([\?].*)?}', 'GET'),
       (110102, '/admin/tickets/user{([\?].*)?}', 'GET'),
       (110103, '/admin/tickets/user/{\\d+([\?].*)?}', 'GET'),
       (110104, '/admin/tickets/user/{\\d+}/close', 'GET'),
       (110105, '/admin/tickets/user/{\\d+}/open', 'GET'),
       (110106, '/admin/tickets/user', 'POST'),
       (110107, '/admin/tickets/user/{\\d+}', 'PUT'),
       (110108, '/admin/tickets/user/{\\d+}', 'DELETE');

-- Ticket  Permission
--Id Range 210050 -210060
INSERT
INTO SECURITY_PERMISSION(ID_PK, PARENT_ID_FK, NODE_TYPE, TRAVERSAL, NAME, TYPE)
VALUES (210050, null, 1, true, 'ticket', 'ADMIN'),
       (210051, 210050, 10, true, 'ticket.read', 'ADMIN'),
       (210052, 210050, 10, true, 'ticket.modify', 'ADMIN');

INSERT
INTO SECURITY_PERMISSION_REST(PERMISSION_ID_FK, REST_ID_FK)
VALUES (210051, 110101),
       (210051, 110102),
       (210051, 110103),
       (210051, 110104),
       (210051, 110105),
       (210052, 110106),
       (210052, 110107),
       (210052, 110108);
------------------------------------------------------------------------------------------------------------------------
-- Ticket Message Detail
-- Id Range 110120 - 110140
INSERT
INTO SECURITY_REST(ID_PK, URL, HTTP_METHOD)
VALUES (110121, '/admin/ticket-messages/user/count{([\?].*)?}', 'GET'),
       (110122, '/admin/ticket-messages/user{([\?].*)?}', 'GET'),
       (110123, '/admin/ticket-messages/user/{\\d+([\?].*)?}', 'GET'),
       (110124, '/admin/ticket-messages/user/ticket/{\\d+}', 'POST'),
       (110125, '/admin/ticket-messages/user/{\\d+}', 'PUT');

-- Ticket Message  Permission
--Id Range 210060 -210070
INSERT
INTO SECURITY_PERMISSION(ID_PK, PARENT_ID_FK, NODE_TYPE, TRAVERSAL, NAME, TYPE)
VALUES (210060, null, 1, true, 'ticket-message', 'ADMIN'),
       (210061, 210060, 10, true, 'ticket-message.read', 'ADMIN'),
       (210062, 210060, 10, true, 'ticket-message.modify', 'ADMIN');

INSERT
INTO SECURITY_PERMISSION_REST(PERMISSION_ID_FK, REST_ID_FK)
VALUES (210061, 110121),
       (210061, 110122),
       (210061, 110123),
       (210062, 110124),
       (210062, 110125);



