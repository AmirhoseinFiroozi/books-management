------------------------------------------------------------------------------------------------------------------------
create table global_property
(
    name     varchar(255) not null,
    profile  varchar(255) not null,
    property jsonb,
    primary key (name, profile)
);

alter table global_property
    owner to postgres;
------------------------------------------------------------------------------------------------------------------------
create table users
(
    id_pk               integer      not null
        primary key,
    username            varchar(201) not null,
    phone_number        varchar(20)  not null,
    email               varchar(254),
    hashed_password     varchar(100) not null,
    lock_expired        timestamp(6),
    access_failed_count integer      not null,
    suspended           boolean      not null,
    created             timestamp(6) not null,
    last_update         timestamp(6),
    deleted             timestamp(6)
);

alter table users
    owner to postgres;

create unique index users_unique_email_not_deleted_key
    on users (email) where (deleted IS NULL);

create unique index users_unique_phone_number_not_deleted_key
    on users (phone_number) where (deleted IS NULL);

create unique index users_unique_user_name_not_deleted_key
    on users (username) where (deleted IS NULL);

create sequence user_seq;

alter sequence user_seq owner to postgres;
------------------------------------------------------------------------------------------------------------------------
create table security_realm
(
    deleted boolean not null,
    id_pk   integer not null
        primary key,
    name    varchar(100)
);

alter table security_realm
    owner to postgres;

create sequence security_realm_seq;

alter sequence security_realm_seq owner to postgres;
------------------------------------------------------------------------------------------------------------------------
create table security_rest
(
    id_pk       integer      not null
        primary key,
    http_method varchar(10)  not null,
    url         varchar(200) not null
);

alter table security_rest
    owner to postgres;
------------------------------------------------------------------------------------------------------------------------
create table security_permission
(
    id_pk        integer not null
        primary key,
    node_type    integer not null,
    parent_id_fk integer
        constraint fkd7tv740unxj87xv8vjpwtjftd
            references security_permission,
    traversal    boolean not null,
    name         varchar(200),
    type         varchar(255)
        constraint security_permission_type_check
            check ((type)::text = ANY ((ARRAY ['ADMIN'::character varying, 'MEMBER'::character varying])::text[]))
    );

alter table security_permission
    owner to postgres;
------------------------------------------------------------------------------------------------------------------------
create table security_permission_rest
(
    permission_id_fk integer not null
        constraint fka21phooipbgfay14ajip6j900
            references security_permission,
    rest_id_fk       integer not null
        constraint fkfuqgv2p62m0ciilvqj8l92ipv
            references security_rest,
    primary key (permission_id_fk, rest_id_fk)
);

alter table security_permission_rest
    owner to postgres;
------------------------------------------------------------------------------------------------------------------------
create table security_role
(
    category integer,
    deleted  boolean              not null,
    id_pk    integer              not null
        primary key,
    show     boolean default true not null,
    type     smallint             not null
        constraint security_role_type_check
            check ((type >= 0) AND (type <= 1)),
    name     varchar(50)
);

alter table security_role
    owner to postgres;

create sequence security_role_seq;

alter sequence security_role_seq owner to postgres;
------------------------------------------------------------------------------------------------------------------------
create table security_role_permission
(
    permission_id_fk integer not null
        constraint fkn10a4yd5buv9rk4jxjregvche
            references security_permission,
    role_id_fk       integer not null
        constraint fkhlaabxfe68ku9vbxns6lqb47r
            references security_role,
    primary key (permission_id_fk, role_id_fk)
);

alter table security_role_permission
    owner to postgres;
------------------------------------------------------------------------------------------------------------------------
create table security_user_role_realm
(
    id_pk         integer not null
        primary key,
    realm_id_fk   integer not null
        constraint fklx8bsy66w3oaf9kue20tloogs
            references security_realm,
    role_id_fk    integer not null
        constraint fk9ixvpf2fv7t0wmlrfxgcb0wep
            references security_role,
    user_id_fk    integer not null
        constraint fka0envboxsx36gvrccoptn4hro
            references users
);

alter table security_user_role_realm
    owner to postgres;
------------------------------------------------------------------------------------------------------------------------
create table user_session
(
    id_pk      integer      not null
        primary key,
    user_id_fk integer
        constraint fks8hvjofe3ioe1no29q7ull505
            references users,
    created    timestamp(6) not null,
    ip         varchar(39)  not null,
    os         varchar(50)  not null,
    agent      varchar(400) not null
);

alter table user_session
    owner to postgres;

create sequence user_session_seq;

alter sequence user_session_seq owner to postgres;
------------------------------------------------------------------------------------------------------------------------
create table book_shelf
(
    id_pk      integer      not null
        primary key,
    user_id_fk integer      not null
        constraint fks8hvjofe3ioe1no29q7ull505
            references users,
    created    timestamp(6) not null,
    name       varchar(255)
);

alter table book_shelf
    owner to postgres;

create sequence book_shelf_seq;

alter sequence book_shelf_seq owner to postgres;
------------------------------------------------------------------------------------------------------------------------
create table book
(
    id_pk            integer      not null
        primary key,
    user_id_fk       integer      not null
        constraint fks8hvjofe3ioe1no29q7ull505
            references users,
    created          timestamp(6) not null,
    name             varchar(255),
    book_shelf_id_fk integer      not null
        constraint book_book_shelf_id_fk_fkey references book_shelf
);

alter table book
    owner to postgres;

create sequence book_seq;

alter sequence book_seq owner to postgres;
------------------------------------------------------------------------------------------------------------------------
--add data for global properties
INSERT INTO global_property(name, profile, property)
values ('ms-books-management', 'dev',
        '{
          "fileCrud": {
            "tempFileUrl": "temp/",
            "baseFilePath": "/opt/books-management/files/",
            "imageQuality": 0.3,
            "tempFilePath": "/opt/books-management/files/temp/",
            "allowedExtensions": []
          },
          "openApiConfig": {
            "name": "Books Management",
            "gitURL": "https://github.com/AmirhoseinFiroozi/books-management.git",
            "description": "Managing Personal Books"
          },
          "identitySettings": {
            "jwt": {
              "refreshPath": "/api/pub/account/refresh",
              "securityKey": "v8y/B?E(H+MbPeShVmYq3t6w9z$C&F)J@NcRfTjWnZr4u7x!A%D*G-KaPdSgVkXp",
              "mobileAccessTokenExpireMin": 2880,
              "browserAccessTokenExpireMin": 2880,
              "mobileRefreshTokenExpireMin": 129600,
              "browserRefreshTokenExpireMin": 129600
            },
            "lockout": {
              "maxFailedAccessAttempts": 8
            },
            "password": {
              "maxLength": 50,
              "requireDigit": false,
              "requiredLength": 8,
              "requireLowercase": false,
              "requireUppercase": false,
              "requiredUniqueChars": 0,
              "requireNonAlphaNumeric": false
            },
            "registration": {
              "registerEnabled": true
            }
          }
        }');
------------------------------------------------------------------------------------------------------------------------
--add initialization query
INSERT INTO USERS (ID_PK, HASHED_PASSWORD, PHONE_NUMBER, EMAIL,
                   CREATED, ACCESS_FAILED_COUNT, LOCK_EXPIRED, SUSPENDED, DELETED, USERNAME)
VALUES (-1, '73l8gRjwLftklgfdXT+MdiMEjJwGPVMsyVxe16iYpk8', '+989129999999',
        'book.local@gmail.com', NOW()::TIMESTAMP, 0,
        NULL, FALSE, NULL, 'ادمین اصلی');

INSERT INTO SECURITY_ROLE (ID_PK, CATEGORY, NAME, DELETED, SHOW, TYPE)
VALUES (-1, NULL, 'ادمین اصلی', FALSE, FALSE, 0);

INSERT INTO SECURITY_REALM (ID_PK, DELETED, NAME)
VALUES (-1, FALSE, 'realm');

INSERT INTO SECURITY_USER_ROLE_REALM (ID_PK, REALM_ID_FK, ROLE_ID_FK, USER_ID_FK)
VALUES (-1, -1, -1, -1);
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

DELETE
FROM SECURITY_ROLE_PERMISSION
WHERE ROLE_ID_FK = -1;

INSERT INTO SECURITY_ROLE_PERMISSION (ROLE_ID_FK, PERMISSION_ID_FK)
SELECT -1, PERMISSION.ID_PK
FROM SECURITY_PERMISSION PERMISSION
WHERE PERMISSION.NODE_TYPE NOT IN (1, 5)
  AND PERMISSION.TYPE IN ('ADMIN', 'MEMBER');