package com.books.user.account.service;

import com.books.security.dto.TokenInfo;
import com.books.security.service.JwtService;
import com.books.user.account.dto.*;
import com.books.user.session.dto.UserSessionIn;
import com.books.user.session.entity.UserSessionEntity;
import com.books.user.session.service.UserSessionService;
import com.books.user.user.entity.UserEntity;
import com.books.user.user.service.UserService;
import com.books.utility.commons.dto.ClientInfo;
import com.books.utility.commons.dto.UserContextDto;
import com.books.utility.config.model.ApplicationProperties;
import com.books.utility.file.service.IFileService;
import com.books.utility.support.HashService;
import com.books.utility.system.exception.SystemError;
import com.books.utility.system.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccountService {
    private final String[] INCLUDE = {"companies"};
    private final ApplicationProperties applicationProperties;
    private final UserService userService;
    private final UserSessionService userSessionService;
    private final HashService hashService;
    private final JwtService jwtService;
    private final IFileService fileService;

    @Autowired
    public AccountService(UserService userService, UserSessionService userSessionService, HashService hashService,
                          ApplicationProperties applicationProperties, JwtService jwtService, IFileService fileService) {
        this.userService = userService;
        this.userSessionService = userSessionService;
        this.hashService = hashService;
        this.applicationProperties = applicationProperties;
        this.jwtService = jwtService;
        this.fileService = fileService;
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
        UserEntity userEntity = userService.getByPhoneNumber(model.getPhoneNumber());
        if (userEntity == null) {
            throw new SystemException(SystemError.USER_NOT_FOUND, "phoneNumber:" + model.getPhoneNumber(), 3001);
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

    public void logout(Integer sessionId) {
        userSessionService.delete(sessionId);
    }

    public LoginOut refresh(UserContextDto userContextDto, Integer sessionId, String token) throws SystemException {
        UserEntity userEntity = userService.getEntityById(userContextDto.getId(), INCLUDE);

        checkUserSuspension(userEntity);

        UserSessionEntity userSessionEntity = this.userSessionService.getEntityById(sessionId, null);
        if (userSessionEntity == null) {
            throw new SystemException(SystemError.USER_NOT_LOGIN, "phoneNumber:" + userEntity.getPhoneNumber(), 3005);
        }
        TokenInfo tokenInfo = jwtService.refresh(token);
        return new LoginOut(userEntity, tokenInfo);
    }


    public UserOut getUser(int id) throws SystemException {
        return new UserOut(userService.getEntityById(id, null));
    }

    public UserImageOut updateUserImage(int id, UserImageIn model) throws SystemException {
        UserEntity userEntity = userService.getEntityById(id, null);
        UserEntity oldModel = userEntity.cloneImages();
        userEntity.setImage(model.getImage());
        fileService.manipulateAttachments(oldModel, userEntity);
        userService.updateEntity(userEntity);
        return new UserImageOut(userEntity);
    }

    private void validateNewPassword(ResetPasswordIn model) throws SystemException {
        if (model.getNewPassword().length() > applicationProperties.getIdentitySettings().getPassword().getMaxLength() ||
                model.getNewPasswordConfirm().length() > applicationProperties.getIdentitySettings().getPassword().getMaxLength() ||
                model.getNewPassword().length() < applicationProperties.getIdentitySettings().getPassword().getRequiredLength() ||
                model.getNewPasswordConfirm().length() < applicationProperties.getIdentitySettings().getPassword().getRequiredLength()) {
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "password length problem", 3012);
        }
    }

    private void checkUserSuspension(UserEntity userEntity) throws SystemException {
        if (userEntity != null) {
            if (userEntity.getLockExpired() != null && userEntity.getLockExpired().isAfter(LocalDateTime.now())) {
                throw new SystemException(SystemError.USER_NOT_ACTIVE, "userId:" + userEntity.getId(), 3002);
            }
        }
    }
}
