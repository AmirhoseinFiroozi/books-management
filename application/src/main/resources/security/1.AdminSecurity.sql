---------------------- Security Rest ----------------------------
--- Id Range 10000
---------------------- Security Permission ----------------------
--- Id Range 10000
------------------------------------------------------------------------------------------------------------------------
-- Security Realms Rest
-- Id Range 10000 - 10050
INSERT
INTO SECURITY_REST(ID_PK, URL, HTTP_METHOD)
VALUES (10001, '/admin/realms/count(([\?].*)?)', 'GET'),
       (10002, '/admin/realms(([\?].*)?)', 'GET'),
       (10003, '/admin/realms/(-)?[0-9]+', 'DELETE'),
       (10004, '/admin/realms', 'POST'),
       (10005, '/admin/realms/(-)?[0-9]+', 'PUT');

-- Security Realms Permission
-- Id Range 10000 - 10050
INSERT
INTO SECURITY_PERMISSION(ID_PK, PARENT_ID_FK, NODE_TYPE, TRAVERSAL, NAME, TYPE)
VALUES (10000, null, 1, true, 'admin', 'ADMIN'),
       (10001, 10000, 5, true, 'admin.realms', 'ADMIN'),
       (10002, 10001, 10, true, 'admin.realms.read', 'ADMIN'),
       (10003, 10001, 10, true, 'admin.realms.modify', 'ADMIN');

INSERT
INTO SECURITY_PERMISSION_REST(PERMISSION_ID_FK, REST_ID_FK)
VALUES (10002, 10001),
       (10002, 10002),
       (10003, 10003),
       (10003, 10004),
       (10003, 10005);
------------------------------------------------------------------------------------------------------------------------
-- Security Permissions Rest
-- Id Range 10050 - 10100
INSERT
INTO SECURITY_REST(ID_PK, URL, HTTP_METHOD)
VALUES (10051, '/admin/permissions/count(([\?].*)?)', 'GET'),
       (10052, '/admin/permissions(([\?].*)?)', 'GET');

-- Security Permissions Permission
-- Id Range 10050 - 10100
INSERT
INTO SECURITY_PERMISSION(ID_PK, PARENT_ID_FK, NODE_TYPE, TRAVERSAL, NAME, TYPE)
VALUES (10050, 10000, 5, true, 'admin.permissions', 'ADMIN'),
       (10051, 10050, 10, true, 'admin.permissions.read', 'ADMIN');

INSERT
INTO SECURITY_PERMISSION_REST(PERMISSION_ID_FK, REST_ID_FK)
VALUES (10051, 10051),
       (10051, 10052);
------------------------------------------------------------------------------------------------------------------------
-- Security Roles Rest
-- Id Range 10100 - 10150
INSERT
INTO SECURITY_REST(ID_PK, URL, HTTP_METHOD)
VALUES (10101, '/admin/roles/count(([\?].*)?)', 'GET'),
       (10102, '/admin/roles(([\?].*)?)', 'GET'),
       (10103, '/admin/roles', 'POST'),
       (10104, '/admin/roles/(-)?[0-9]+', 'PUT'),
       (10105, '/admin/roles/(-)?[0-9]+', 'DELETE');

-- Security Roles Permission
-- Id Range 10100 - 10150
INSERT
INTO SECURITY_PERMISSION(ID_PK, PARENT_ID_FK, NODE_TYPE, TRAVERSAL, NAME, TYPE)
VALUES (10100, 10000, 5, true, 'admin.roles', 'ADMIN'),
       (10101, 10100, 10, true, 'admin.roles.read', 'ADMIN'),
       (10102, 10100, 10, true, 'admin.roles.modify', 'ADMIN');

INSERT
INTO SECURITY_PERMISSION_REST(PERMISSION_ID_FK, REST_ID_FK)
VALUES (10101, 10101),
       (10101, 10102),
       (10102, 10103),
       (10102, 10104),
       (10102, 10105);
------------------------------------------------------------------------------------------------------------------------
-- Security Users Rest
-- Id Range 10150 - 10200
INSERT
INTO SECURITY_REST(ID_PK, URL, HTTP_METHOD)
VALUES (10151, '/admin/users/count(([\?].*)?)', 'GET'),
       (10152, '/admin/users(([\?].*)?)', 'GET'),
       (10153, '/admin/users/(-)?[0-9]+', 'GET'),
       (10154, '/admin/users/(-)?[0-9]+', 'PUT'),
       (10155, '/admin/users/(-)?[0-9]+/role', 'PATCH'),
       (10156, '/admin/users/(-)?[0-9]+', 'DELETE');

-- Security Users Permission
-- Id Range 10150 - 10200
INSERT
INTO SECURITY_PERMISSION(ID_PK, PARENT_ID_FK, NODE_TYPE, TRAVERSAL, NAME, TYPE)
VALUES (10150, 10000, 5, true, 'admin.users', 'ADMIN'),
       (10151, 10150, 10, true, 'admin.users.read', 'ADMIN'),
       (10152, 10150, 10, true, 'admin.users.modify', 'ADMIN');

INSERT
INTO SECURITY_PERMISSION_REST(PERMISSION_ID_FK, REST_ID_FK)
VALUES (10151, 10151),
       (10151, 10152),
       (10151, 10153),
       (10152, 10154),
       (10152, 10155),
       (10152, 10156);