package com.books.security.realm.service;

import com.books.security.realm.entity.SecurityRealmEntity;
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
public class BaseRealmService extends AbstractService<SecurityRealmEntity, Dao<SecurityRealmEntity>> {

    @Autowired
    public BaseRealmService() {

    }
}
