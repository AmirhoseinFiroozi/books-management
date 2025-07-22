package com.books.user.user.repository;

import com.books.database.repository.Dao;
import com.books.user.user.entity.UserEntity;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BaseUserDao extends Dao<UserEntity> {
    public UserEntity getByPhoneNumber(String phoneNumber) {
        Query query = this.getEntityManager().createQuery("SELECT user FROM UserEntity user " +
                "left join fetch user.roleRealms roleRealms " +
                "left join fetch roleRealms.role role " +
                "left join fetch role.permissions " +
                "where user.phoneNumber = :phoneNumber AND user.deleted is null");
        Map<String, Object> map = new HashMap<>();
        map.put("phoneNumber", phoneNumber);
        List<UserEntity> result = super.queryHql(query, map);
        return result.isEmpty() ? null : result.getFirst();
    }

    public void updateAccessFailedCount(int id) {
        Query query = this.getEntityManager().createQuery("update UserEntity user " +
                "set user.accessFailedCount = 0 " +
                "where user.id=:id");
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        super.updateHqlQuery(query, map);
    }

}
