package com.books.application.user.account.service;

import com.books.application.user.account.dto.*;
import com.books.application.user.session.dto.UserSessionIn;
import com.books.application.user.session.entity.UserSessionEntity;
import com.books.application.user.session.service.UserSessionService;
import com.books.application.user.user.entity.UserEntity;
import com.books.application.user.user.service.UserService;
import com.books.security.dto.TokenInfo;
import com.books.security.service.JwtService;
import com.books.utility.commons.dto.ClientInfo;
import com.books.utility.commons.dto.UserContextDto;
import com.books.utility.config.model.ApplicationProperties;
import com.books.utility.support.HashService;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class AccountService {
    private final String[] INCLUDE = {"companies"};
    private final ApplicationProperties applicationProperties;
    private final UserService userService;
    private final UserSessionService userSessionService;
    private final HashService hashService;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    @Autowired
    public AccountService(UserService userService, UserSessionService userSessionService, HashService hashService,
                          ApplicationProperties applicationProperties, JwtService jwtService, ModelMapper modelMapper) {
        this.userService = userService;
        this.userSessionService = userSessionService;
        this.hashService = hashService;
        this.applicationProperties = applicationProperties;
        this.jwtService = jwtService;
        this.modelMapper = modelMapper;
    }

    private LoginOut login(UserEntity userEntity, ClientInfo clientInfo) throws SystemException {
        UserSessionIn userSessionIn = new UserSessionIn();
        userSessionIn.setUserId(userEntity.getId());
        userSessionIn.setAgent(clientInfo.getAgent());
        userSessionIn.setIp(clientInfo.getMainIP());
        userSessionIn.setOs(clientInfo.getClientOS());

        UserSessionEntity userSession = userSessionService.getExistingSessionOrCreateNewSession(userSessionIn);
        TokenInfo tokenInfo = this.jwtService.create(userEntity.getId(), userSession.getId());

        return new LoginOut(userEntity, tokenInfo);
    }

    public LoginOut login(LoginIn model, ClientInfo clientInfo) throws SystemException {
        UserEntity userEntity = userService.getByUsernameOrPhoneNumber(model.getUsername());
        if (userEntity == null) {
            throw new SystemException(SystemError.USER_NOT_FOUND, "username:" + model.getUsername(), 3001);
        }

        checkUserSuspension(userEntity);

        String securePassword = this.hashService.hash(model.getPassword());
        if (!securePassword.equals(userEntity.getHashedPassword())) {
            userEntity.setAccessFailedCount(userEntity.getAccessFailedCount() + 1);
            if (userEntity.getAccessFailedCount() >= applicationProperties.getIdentitySettings().getLockout().getMaxFailedAccessAttempts()) {
                userEntity.setLockExpired(LocalDateTime.now().plusMinutes(5));
                userEntity.setAccessFailedCount(0);
            }
            userService.updateEntity(userEntity);
            throw new SystemException(SystemError.USERNAME_PASSWORD_NOT_MATCH, "", 3003);
        }

        if (userEntity.getAccessFailedCount() != 0) {
            userService.updateAccessFailedCount(userEntity.getId());
        }
        return login(userEntity, clientInfo);
    }

    public LoginOut register(UserRegisterIn model, ClientInfo clientInfo) throws SystemException {
        if (!applicationProperties.getIdentitySettings().getRegistration().isRegisterEnabled()) {
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "register: " + false, 3010);
        }
        if (userService.existsByUsernameOrPhoneNumber(model.getUsername(), model.getPhoneNumber())) {
            throw new SystemException(SystemError.USERNAME_ALREADY_EXIST, "username: " + model.getUsername() + " or, phoneNumber: " + model.getPhoneNumber(), 3001);
        }
        UserEntity entity = new UserEntity();
        modelMapper.map(model, entity);
        entity.setHashedPassword(hashService.hash(model.getPassword()));
        userService.createEntity(entity);
        return login(entity, clientInfo);
    }

    public void changePassword(int userId, ChangePasswordIn model) throws SystemException {
        UserEntity entity = userService.getEntityById(userId, null);
        if (!Objects.equals(hashService.hash(model.getCurrentPassword()), entity.getHashedPassword())) {
            throw new SystemException(SystemError.USERNAME_PASSWORD_NOT_MATCH, "id: " + userId, 3022);
        }
        entity.setHashedPassword(hashService.hash(model.getNewPassword()));
        userService.updateEntity(entity);
    }

    public UserOut update(int userId, UserEditIn model) throws SystemException {
        if (userService.existsByUsernameOrPhoneNumber(model.getUsername(), model.getPhoneNumber())) {
            throw new SystemException(SystemError.USERNAME_ALREADY_EXIST, "username: " + model.getUsername() + ", phoneNumber: " + model.getPhoneNumber(), 3001);
        }
        UserEntity entity = userService.getEntityById(userId, null);
        modelMapper.map(model, entity);
        userService.updateEntity(entity);
        return new UserOut(entity);
    }

    public void logout(Integer sessionId) {
        userSessionService.delete(sessionId);
    }

    public LoginOut refresh(UserContextDto userContextDto, String token) throws SystemException {
        UserEntity userEntity = userService.getEntityById(userContextDto.getId(), INCLUDE);

        checkUserSuspension(userEntity);

        UserSessionEntity userSessionEntity = this.userSessionService.getEntityById(userContextDto.getSessionId(), null);
        if (userSessionEntity == null) {
            throw new SystemException(SystemError.USER_NOT_LOGIN, "phoneNumber:" + userEntity.getPhoneNumber(), 3005);
        }
        TokenInfo tokenInfo = jwtService.refresh(token);
        return new LoginOut(userEntity, tokenInfo);
    }


    public UserOut getUser(int id) throws SystemException {
        return new UserOut(userService.getEntityById(id, null));
    }

    private void checkUserSuspension(UserEntity userEntity) throws SystemException {
        if (userEntity != null) {
            if (userEntity.getLockExpired() != null && userEntity.getLockExpired().isAfter(LocalDateTime.now())) {
                throw new SystemException(SystemError.USER_NOT_ACTIVE, "userId:" + userEntity.getId(), 3002);
            }
        }
    }
}
