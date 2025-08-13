package com.books.domain.user.session.repository;

import com.books.database.repository.Dao;
import com.books.domain.user.session.entity.UserSessionEntity;
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
}
