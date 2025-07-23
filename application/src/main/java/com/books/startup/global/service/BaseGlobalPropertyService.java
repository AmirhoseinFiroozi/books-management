package com.books.startup.global.service;

import com.books.database.service.AbstractService;
import com.books.startup.global.dto.GlobalPropertyEditeIn;
import com.books.startup.global.entity.GlobalPropertyEntity;
import com.books.startup.global.repository.BaseGlobalPropertyDao;
import com.books.utility.system.exception.SystemException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther : Armin.Nik
 * @project : shared
 * @date : 04.10.22
 */
@Service
public class BaseGlobalPropertyService extends AbstractService<GlobalPropertyEntity, BaseGlobalPropertyDao> {
    private final ModelMapper modelMapper;

    @Autowired
    public BaseGlobalPropertyService(BaseGlobalPropertyDao dao, ModelMapper modelMapper) {
        super(dao);
        this.modelMapper = modelMapper;
    }

    public List<GlobalPropertyEntity> getAllEntities() {
        return getDao().getAllEntities();
    }

    public GlobalPropertyEntity update(GlobalPropertyEditeIn editeIn, String name, String profile) throws SystemException {
        GlobalPropertyEntity globalProperty = getById(name, profile);
        modelMapper.map(editeIn, globalProperty);
        return updateEntity(globalProperty);
    }

    public GlobalPropertyEntity getById(String name, String profile) {
        return getDao().getEntityById(name, profile);
    }
}
