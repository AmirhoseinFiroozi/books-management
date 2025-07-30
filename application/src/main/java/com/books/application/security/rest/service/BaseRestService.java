package com.books.application.security.rest.service;

import com.books.application.security.rest.entity.SecurityRestEntity;
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
public class BaseRestService extends AbstractService<SecurityRestEntity, Dao<SecurityRestEntity>> {

    @Autowired
    public BaseRestService() {

    }
}
