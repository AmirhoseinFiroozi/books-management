package com.books.application.security.realm.service;

import com.books.application.security.realm.dto.RealmIn;
import com.books.application.security.realm.dto.RealmOut;
import com.books.application.security.realm.entity.SecurityRealmEntity;
import com.books.application.security.realm.repository.BaseSecurityRealmDao;
import com.books.database.service.AbstractService;
import com.books.utility.commons.repository.dto.ReportCondition;
import com.books.utility.commons.repository.dto.ReportFilter;
import com.books.utility.commons.repository.dto.ReportOption;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther : Armin.Nik
 * @project : shared
 * @date : 16.11.21
 */
@Service
public class BaseSecurityRealmService extends AbstractService<SecurityRealmEntity, BaseSecurityRealmDao> {

    @Autowired
    public BaseSecurityRealmService(BaseSecurityRealmDao dao) {
        super(dao);
    }

    public int count() {
        return countEntity(new ReportFilter(new ReportCondition(), new ReportOption()));
    }

    public List<RealmOut> getAll() {
        ReportOption reportOption = new ReportOption();
        reportOption.setExport(true);
        return getAllEntities(new ReportFilter(new ReportCondition(), reportOption), null)
                .stream().map(RealmOut::new).toList();
    }

    public void create(RealmIn model) {
        SecurityRealmEntity entity = new SecurityRealmEntity();
        entity.setName(model.getName());
        createEntity(entity);
    }

    public void update(int id, RealmIn model) throws SystemException {
        SecurityRealmEntity entity = getEntityById(id, null);
        entity.setName(model.getName());
        updateEntity(entity);
    }

    public void delete(int id) throws SystemException {
        if (getDao().relationExists(id)) {
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "there is a user related to this realm", 100011);
        }
        deleteById(id);
    }
}
