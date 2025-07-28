package com.books.security.permission.service;

import com.books.database.repository.Dao;
import com.books.database.service.AbstractService;
import com.books.security.permission.entity.SecurityPermissionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @auther : Armin.Nik
 * @project : shared
 * @date : 16.11.21
 */
@Service
public class BasePermissionService extends AbstractService<SecurityPermissionEntity, Dao<SecurityPermissionEntity>> {

    @Autowired
    public BasePermissionService() {

    }
}
