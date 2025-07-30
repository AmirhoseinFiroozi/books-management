package com.books.application.user.session.service;

import com.books.database.service.AbstractService;
import com.books.application.user.session.dto.UserSessionIn;
import com.books.application.user.session.entity.UserSessionEntity;
import com.books.application.user.session.repository.BaseUserSessionDao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserSessionService extends AbstractService<UserSessionEntity, BaseUserSessionDao> {
    private final ModelMapper modelMapper;

    @Autowired
    public UserSessionService(BaseUserSessionDao dao, ModelMapper modelMapper) {
        super(dao);
        this.modelMapper = modelMapper;
    }


    public UserSessionEntity getExistingSessionOrCreateNewSession(UserSessionIn model) {
        UserSessionEntity userSessionEntity = getDao().getByUniqueId(model.getUserId());
        if (userSessionEntity == null) {
            userSessionEntity = modelMapper.map(model, UserSessionEntity.class);
            userSessionEntity.setCreated(LocalDateTime.now());
            super.createEntity(userSessionEntity);
        } else {
            modelMapper.map(model, userSessionEntity);
            super.updateEntity(userSessionEntity);
        }
        return userSessionEntity;
    }

    public boolean delete(int id) {
        return super.deleteById(id);
    }
}
