package com.books.application.user.session.repository;

import com.books.database.repository.Dao;
import com.books.application.user.session.entity.UserSessionEntity;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class BaseUserSessionDao extends Dao<UserSessionEntity> {

    public UserSessionEntity getByUniqueId(Integer userId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", userId);
        return super.getByAndConditions(parameters);
    }

    public void deleteByUserId(int userId) {
        Query query = this.getEntityManager().createQuery("delete from UserSessionEntity session where session.userId=:userId");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", userId);
        updateHqlQuery(query, parameters);
    }
}
