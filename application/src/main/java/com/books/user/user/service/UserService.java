package com.books.user.user.service;

import com.books.database.service.AbstractService;
import com.books.user.user.entity.UserEntity;
import com.books.user.user.repository.BaseUserDao;
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

    public void updateAccessFailedCount(int id) {
        getDao().updateAccessFailedCount(id);
    }

}