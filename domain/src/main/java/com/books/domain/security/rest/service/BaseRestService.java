package com.books.domain.security.rest.service;

import com.books.database.repository.Dao;
import com.books.database.service.AbstractService;
import com.books.domain.security.rest.entity.SecurityRestEntity;
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
