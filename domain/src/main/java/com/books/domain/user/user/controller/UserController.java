package com.books.domain.user.user.controller;

import com.books.domain.user.user.dto.*;
import com.books.domain.user.user.service.UserService;
import com.books.domain.user.user.statics.constants.UserRestApi;
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

import java.util.List;

@Tag(name = "Admin User Controller")
@RestController
@RequestMapping(value = {"${rest.admin}"})
@Validated
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(description = "Count All Users")
    @GetMapping(path = UserRestApi.USERS_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> count(@Valid UserFilter filter, BindingResult bindingResult, HttpServletRequest request) {
        return new ResponseEntity<>(userService.count(filter), HttpStatus.OK);
    }

    @Operation(description = "Get All Users")
    @GetMapping(path = UserRestApi.USERS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserInfoOut>> getAllInfo(@Valid UserPageableFilter pageableFilter, BindingResult bindingResult,
                                                        HttpServletRequest request) {
        return new ResponseEntity<>(userService.getAllInfo(pageableFilter), HttpStatus.OK);
    }

    @Operation(description = "Get User by id")
    @GetMapping(path = UserRestApi.USERS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserOut> getById(@PathVariable(name = "id") int id, HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @Operation(description = "update User by id")
    @PutMapping(path = UserRestApi.USERS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserOut> update(@RequestBody @Valid UserIn model, BindingResult bindingResult,
                                          @PathVariable(name = "id") int id, HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(userService.update(id, model), HttpStatus.OK);
    }

    @Operation(description = "update User's roles by id")
    @PatchMapping(path = UserRestApi.USERS_ID_ROLE_REALM, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void updateUserRoles(@RequestBody @Valid List<UserRoleRealmIn> model, BindingResult bindingResult,
                                @PathVariable(name = "id") int id, HttpServletRequest request) throws SystemException {
        userService.updateUserRoleRealms(id, model);
    }

    @Operation(description = "delete user by id")
    @DeleteMapping(path = UserRestApi.USERS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable(name = "id") int id, HttpServletRequest request) throws SystemException {
        userService.delete(id);
    }
}
