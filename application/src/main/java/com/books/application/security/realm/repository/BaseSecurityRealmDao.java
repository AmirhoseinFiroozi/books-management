package com.books.application.security.realm.repository;

import com.books.application.book.book.entity.BookEntity;
import com.books.application.security.realm.entity.SecurityRealmEntity;
import com.books.database.repository.Dao;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BaseSecurityRealmDao extends Dao<SecurityRealmEntity> {
    public boolean relationExists(int id) {
        Query query = this.getEntityManager().createQuery("SELECT 1 FROM SecurityUserRoleRealmEntity entity " +
                "where entity.realmId = :id");
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        List<BookEntity> result = super.queryHql(query, map, 1);
        return !result.isEmpty();
    }
}
