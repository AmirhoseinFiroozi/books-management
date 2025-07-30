package com.books.application.startup.global.repository;

import com.books.database.repository.Dao;
import com.books.application.startup.global.entity.GlobalPropertyEntity;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther : Armin.Nik
 * @project : shared
 * @date : 04.10.22
 */
@Repository
public class BaseGlobalPropertyDao extends Dao<GlobalPropertyEntity> {

    public List<GlobalPropertyEntity> getAllEntities() {
        Query query = this.getEntityManager().createQuery(" SELECT property FROM GlobalPropertyEntity property ");
        Map<String, Object> map = new HashMap<>();
        List<GlobalPropertyEntity> result = super.queryHql(query, map);
        return result.isEmpty() ? new ArrayList<>(0) : result;
    }

    //this is composite id
    public GlobalPropertyEntity getEntityById(String name, String profile) {
        Query query = this.getEntityManager().createQuery(" SELECT property FROM GlobalPropertyEntity property " +
                "WHERE property.id.profile = :profile AND property.id.name = :name ");
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("profile", profile);
        List<GlobalPropertyEntity> result = super.queryHql(query, map);
        return result.isEmpty() ? null : result.get(0);
    }

}
