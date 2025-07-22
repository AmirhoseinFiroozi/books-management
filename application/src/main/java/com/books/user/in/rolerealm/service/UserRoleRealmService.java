package com.books.user.in.rolerealm.service;

import com.books.database.repository.Dao;
import com.books.database.service.AbstractService;
import com.books.user.in.rolerealm.entity.SecurityUserRoleRealmEntity;
import org.springframework.stereotype.Service;

@Service
public class UserRoleRealmService extends AbstractService<SecurityUserRoleRealmEntity, Dao<SecurityUserRoleRealmEntity>> {
}
