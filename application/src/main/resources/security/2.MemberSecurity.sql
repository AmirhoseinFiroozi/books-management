---------------------- Security Rest ----------------------------
--- Id Range 1
---------------------- Security Permission ----------------------
--- Id Range 1
------------------------------------------------------------------------------------------------------------------------
-- Security BookShelves Rest
-- Id Range 1 - 50
INSERT
INTO SECURITY_REST(ID_PK, URL, HTTP_METHOD)
VALUES (2, '/member/book-shelves/count(([\?].*)?)', 'GET'),
       (3, '/member/book-shelves(([\?].*)?)', 'GET'),
       (4, '/member/book-shelves', 'POST'),
       (5, '/member/book-shelves/(-)?[0-9]+', 'PUT'),
       (6, '/member/book-shelves/(-)?[0-9]+', 'DELETE');

-- Security BookShelves Permission
-- Id Range 1 - 50
INSERT
INTO SECURITY_PERMISSION(ID_PK, PARENT_ID_FK, NODE_TYPE, TRAVERSAL, NAME, TYPE)
VALUES (1, null, 1, true, 'member', 'MEMBER'),
       (2, 1, 5, true, 'member.book-shelves', 'MEMBER'),
       (3, 2, 10, true, 'member.book-shelves.read', 'MEMBER'),
       (4, 2, 10, true, 'member.book-shelves.modify', 'MEMBER');

INSERT
INTO SECURITY_PERMISSION_REST(PERMISSION_ID_FK, REST_ID_FK)
VALUES (3, 2),
       (3, 3),
       (4, 4),
       (4, 5),
       (4, 6);
------------------------------------------------------------------------------------------------------------------------
-- Security Books Rest
-- Id Range 50 - 100
INSERT
INTO SECURITY_REST(ID_PK, URL, HTTP_METHOD)
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
INTO SECURITY_PERMISSION(ID_PK, PARENT_ID_FK, NODE_TYPE, TRAVERSAL, NAME, TYPE)
VALUES (50, 1, 5, true, 'member.books', 'MEMBER'),
       (51, 50, 10, true, 'member.books.read', 'MEMBER'),
       (52, 50, 10, true, 'member.books.modify', 'MEMBER');

INSERT
INTO SECURITY_PERMISSION_REST(PERMISSION_ID_FK, REST_ID_FK)
VALUES (51, 51),
       (51, 52),
       (51, 53),
       (51, 54),
       (52, 55),
       (52, 56),
       (52, 57),
       (52, 58);