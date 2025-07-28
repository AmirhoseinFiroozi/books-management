package com.books.user.account.controller;

import com.books.security.authentication.filter.JwtUser;
import com.books.security.service.AccessService;
import com.books.user.account.dto.*;
import com.books.user.account.service.AccountService;
import com.books.user.account.statics.AccountRestApi;
import com.books.utility.commons.dto.ClientInfo;
import com.books.utility.commons.dto.UserContextDto;
import com.books.utility.system.exception.SystemException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Account")
@RestController
@RequestMapping
@Validated
public class AccountController {
    private final AccessService accessService;
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService, AccessService accessService) {
        this.accountService = accountService;
        this.accessService = accessService;
    }

    @Operation(description = "User Login by username and password")
    @PostMapping(path = {"${rest.public}" + AccountRestApi.LOGIN}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginOut> login(@RequestBody @Valid LoginIn model, BindingResult bindingResult, HttpServletRequest request) throws SystemException {
        ClientInfo clientInfo = new ClientInfo(request);
        return new ResponseEntity<>(accountService.login(model, clientInfo), HttpStatus.OK);
    }

    @Operation(description = "Refresh Token")
    @GetMapping(path = {"${rest.public}" + AccountRestApi.REFRESH_TOKEN}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginOut> refresh(HttpServletRequest request) throws SystemException {
        int sessionId = accessService.getSessionIdFromRefreshToken(request);
        String token = accessService.getAuthenticatedToken(request);
        UserContextDto userContextDto = accessService.getAuthenticatedUserFromRefreshToken(request);
        return new ResponseEntity<>(accountService.refresh(userContextDto, sessionId, token), HttpStatus.OK);
    }

    @Operation(description = "User Logout")
    @PostMapping(path = {"${rest.public}" + AccountRestApi.LOGOUT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest request) throws SystemException {
        int sessionId = accessService.getSessionIdFromAccessToken(request);
        accountService.logout(sessionId);
    }

    @Operation(description = "Get user's info")
    @GetMapping(path = {"${rest.identified}" + AccountRestApi.USERS}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserOut> getUser(HttpServletRequest request) throws SystemException {
        UserContextDto contextDto = JwtUser.getAuthenticatedUser();
        return new ResponseEntity<>(accountService.getUser(contextDto.getId()), HttpStatus.OK);
    }
}