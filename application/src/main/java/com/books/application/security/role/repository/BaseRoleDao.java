package com.books.application.security.role.repository;

import com.books.application.book.book.entity.BookEntity;
import com.books.application.security.role.entity.SecurityRoleEntity;
import com.books.database.repository.Dao;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BaseRoleDao extends Dao<SecurityRoleEntity> {
    public boolean hasUser(int id) {
        Query query = this.getEntityManager().createQuery("SELECT 1 FROM SecurityUserRoleRealmEntity entity " +
                "where entity.roleId = :id");
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        List<BookEntity> result = super.queryHql(query, map, 1);
        return !result.isEmpty();
    }

    public boolean hasPermission(int id) {
        String sql = "SELECT 1\n" +
                "FROM security_role_permission\n" +
                "where role_id_fk = :id";
        org.hibernate.query.Query<Integer> query = getSession().createNativeQuery(sql, Integer.class);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        List<Integer> result = querySql(query, parameters);
        return !result.isEmpty();
    }
}
