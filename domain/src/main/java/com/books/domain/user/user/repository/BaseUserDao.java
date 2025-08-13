package com.books.domain.user.user.repository;

import com.books.database.repository.Dao;
import com.books.domain.user.user.entity.UserEntity;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BaseUserDao extends Dao<UserEntity> {
    public UserEntity getByUsernameOrPhoneNumber(String username) {
        Query query = this.getEntityManager().createQuery("SELECT user FROM UserEntity user " +
                "left join fetch user.roleRealms roleRealms " +
                "left join fetch roleRealms.role role " +
                "left join fetch role.permissions " +
                "where (user.phoneNumber = :username OR user.username = :username) AND user.deleted is null");
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        List<UserEntity> result = super.queryHql(query, map);
        return result.isEmpty() ? null : result.getFirst();
    }

    public boolean existsByUsernameOrPhoneNumber(String username, String phoneNumber) {
        Query query = this.getEntityManager().createQuery("SELECT 1 FROM UserEntity user " +
                "where (user.phoneNumber = :phoneNumber OR user.username = :username) AND user.deleted is null");
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("phoneNumber", phoneNumber);
        List<Integer> result = super.queryHql(query, map);
        return !result.isEmpty();
    }

    public UserEntity getByUsernameOrPhoneNumber(String username, String phoneNumber) {
        Query query = this.getEntityManager().createQuery("SELECT user FROM UserEntity user " +
                "where (user.phoneNumber = :phoneNumber OR user.username = :username) AND user.deleted is null");
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
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
