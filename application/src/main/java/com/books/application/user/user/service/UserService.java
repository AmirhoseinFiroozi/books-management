package com.books.application.user.user.service;

import com.books.application.user.user.entity.UserEntity;
import com.books.application.user.user.repository.BaseUserDao;
import com.books.database.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractService<UserEntity, BaseUserDao> {
    @Autowired
    public UserService(BaseUserDao dao) {
        super(dao);
    }

    public UserEntity getByPhoneNumber(String phoneNumber) {
        return getDao().getByPhoneNumber(phoneNumber);
    }

    public UserEntity getByUsernameOrPhoneNumber(String username) {
        return getDao().getByUsernameOrPhoneNumber(username);
    }

    public boolean existsByUsernameOrPhoneNumber(String username, String phoneNumber) {
        return getDao().existsByUsernameOrPhoneNumber(username, phoneNumber);
    }

    public void updateAccessFailedCount(int id) {
        getDao().updateAccessFailedCount(id);
    }

}