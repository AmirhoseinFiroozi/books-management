package com.books.application.security.role.service;

import com.books.application.security.role.entity.SecurityRoleEntity;
import com.books.database.service.AbstractService;
import com.books.database.repository.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @auther : Armin.Nik
 * @project : shared
 * @date : 16.11.21
 */
@Service
public class BaseRoleService extends AbstractService<SecurityRoleEntity, Dao<SecurityRoleEntity>> {

    @Autowired
    public BaseRoleService() {

    }
}
