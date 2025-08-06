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