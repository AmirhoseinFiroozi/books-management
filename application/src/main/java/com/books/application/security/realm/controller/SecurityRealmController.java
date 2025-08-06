package com.books.application.security.realm.controller;

import com.books.application.security.realm.dto.RealmIn;
import com.books.application.security.realm.dto.RealmOut;
import com.books.application.security.realm.service.BaseSecurityRealmService;
import com.books.application.security.realm.statics.constants.SecurityRealmRestApi;
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

@Tag(name = "Admin Realm Controller")
@RestController
@RequestMapping(value = {"${rest.admin}"})
@Validated
public class SecurityRealmController {
    private final BaseSecurityRealmService securityRealmService;

    @Autowired
    public SecurityRealmController(BaseSecurityRealmService securityRealmService) {
        this.securityRealmService = securityRealmService;
    }

    @Operation(description = "count realms")
    @GetMapping(path = SecurityRealmRestApi.REALMS_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> count(HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(securityRealmService.count(), HttpStatus.OK);
    }

    @Operation(description = "get all realms")
    @GetMapping(path = SecurityRealmRestApi.REALMS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RealmOut>> getAll(HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(securityRealmService.getAll(), HttpStatus.OK);
    }

    @Operation(description = "create realm")
    @PostMapping(path = SecurityRealmRestApi.REALMS, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void create(@RequestBody @Valid RealmIn model, BindingResult bindingResult,
                       HttpServletRequest httpServletRequest) throws SystemException {
        securityRealmService.create(model);
    }

    @Operation(description = "update realm")
    @PutMapping(path = SecurityRealmRestApi.REALMS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable(name = "id") int id, @RequestBody @Valid RealmIn model, BindingResult bindingResult,
                       HttpServletRequest httpServletRequest) throws SystemException {
        securityRealmService.update(id, model);
    }

    @Operation(description = "delete realm by id")
    @DeleteMapping(path = SecurityRealmRestApi.REALMS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") int id, HttpServletRequest httpServletRequest) throws SystemException {
        securityRealmService.delete(id);
    }
}
