--add initialization query
INSERT INTO vrp.USERS (ID_PK, HASHED_PASSWORD, PHONE_NUMBER, EMAIL,
                       CREATED, ACCESS_FAILED_COUNT, LOCK_EXPIRED, SUSPENDED, DELETED, FULL_NAME)
VALUES (-1, E '73l8gRjwLftklgfdXT+MdiMEjJwGPVMsyVxe16iYpk8', '+989129999999',
        E 'book.local@gmail.com', '2019-08-05 11:11:06.552000', 0,
        NULL, FALSE, NULL, 'ادمین اصلی');

INSERT INTO vrp.SECURITY_ROLE (ID_PK, CATEGORY, NAME, DELETED, SHOW, TYPE)
VALUES (-1, NULL, E 'ادمین اصلی', FALSE, FALSE, 0);

INSERT INTO vrp.USER_SESSION(ID_PK, AGENT, CREATED, IP, OS, USER_ID_FK)
VALUES (-1, 'Mozilla', '2020-09-06 10:56:34.086', '91.92.205.199', 'Unix', -1);

INSERT INTO vrp.SECURITY_REALM (ID_PK, DELETED, NAME)
VALUES (-1, FALSE, 'realm');

INSERT INTO vrp.SECURITY_USER_ROLE_REALM (ID_PK, REALM_ID_FK, ROLE_ID_FK, USER_ID_FK)
VALUES (-1, -1, -1, -1);
-----------------------------------------------------------------------------------------------------------------------
--add users unique indexes
create unique index users_unique_email_not_deleted_key
    on users (email) where (deleted IS NULL);

create unique index users_unique_phone_number_not_deleted_key
    on users (phone_number) where (deleted IS NULL);

create unique index users_unique_user_name_not_deleted_key
    on users (user_name) where (deleted IS NULL);
------------------------------------------------------------------------------------------------------------------------
-- Security BookShelves Rest
-- Id Range 1 - 50
INSERT
INTO vrp.SECURITY_REST(ID_PK, URL, HTTP_METHOD)
VALUES (2, '/member/book-shelves/count(([\?].*)?)', 'GET'),
       (3, '/member/book-shelves(([\?].*)?)', 'GET'),
       (4, '/member/book-shelves', 'POST'),
       (5, '/member/book-shelves/(-)?[0-9]+', 'PUT'),
       (6, '/member/book-shelves/(-)?[0-9]+', 'DELETE');

-- Security BookShelves Permission
-- Id Range 1 - 50
INSERT
INTO vrp.SECURITY_PERMISSION(ID_PK, PARENT_ID_FK, NODE_TYPE, TRAVERSAL, NAME, TYPE)
VALUES (1, null, 1, true, 'member', 'MEMBER'),
       (2, 1, 5, true, 'member.book-shelves', 'MEMBER'),
       (3, 2, 10, true, 'member.book-shelves.read', 'MEMBER'),
       (4, 2, 10, true, 'member.book-shelves.modify', 'MEMBER');

INSERT
INTO vrp.SECURITY_PERMISSION_REST(PERMISSION_ID_FK, REST_ID_FK)
VALUES (3, 2),
       (3, 3),
       (4, 4),
       (4, 5),
       (4, 6);
------------------------------------------------------------------------------------------------------------------------
-- Security Books Rest
-- Id Range 50 - 100
INSERT
INTO vrp.SECURITY_REST(ID_PK, URL, HTTP_METHOD)
VALUES (51, '/member/books/count(([\?].*)?)', 'GET'),
       (52, '/member/books(([\?].*)?)', 'GET'),
       (53, '/member/books/(-)?[0-9]+', 'GET'),
       (54, '/member/books/(-)?[0-9]+/file', 'GET'),
       (55, '/member/books', 'POST'),
       (56, '/member/books/(-)?[0-9]+', 'PUT'),
       (57, '/member/books/(-)?[0-9]+/file', 'PUT'),
       (58, '/member/books/(-)?[0-9]+', 'DELETE');

-- Security Books Permission
-- Id Range 50 - 100
INSERT
INTO vrp.SECURITY_PERMISSION(ID_PK, PARENT_ID_FK, NODE_TYPE, TRAVERSAL, NAME, TYPE)
VALUES (50, 1, 5, true, 'member.books', 'MEMBER'),
       (51, 50, 10, true, 'member.books.read', 'MEMBER'),
       (52, 50, 10, true, 'member.books.modify', 'MEMBER');

INSERT
INTO vrp.SECURITY_PERMISSION_REST(PERMISSION_ID_FK, REST_ID_FK)
VALUES (51, 51),
       (51, 52),
       (51, 53),
       (51, 54),
       (52, 55),
       (52, 56),
       (52, 57),
       (52, 58);

DELETE
FROM vrp.SECURITY_ROLE_PERMISSION
WHERE ROLE_ID_FK = -1;

INSERT INTO vrp.SECURITY_ROLE_PERMISSION (ROLE_ID_FK, PERMISSION_ID_FK)
SELECT -1, PERMISSION.ID_PK
FROM vrp.SECURITY_PERMISSION PERMISSION
WHERE PERMISSION.NODE_TYPE NOT IN (1, 5)
  AND PERMISSION.TYPE IN ('ADMIN', 'MEMBER');