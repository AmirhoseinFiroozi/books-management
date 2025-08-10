package com.books.application.security.permission.controller;

import com.books.application.security.permission.dto.PermissionFilter;
import com.books.application.security.permission.dto.PermissionOut;
import com.books.application.security.permission.dto.PermissionPageableFilter;
import com.books.application.security.permission.service.BasePermissionService;
import com.books.application.security.permission.statics.constants.SecurityPermissionRestApi;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Admin Permission Controller")
@RestController
@RequestMapping(value = {"${rest.admin}"})
@Validated
public class SecurityPermissionController {
    private final BasePermissionService permissionService;

    @Autowired
    public SecurityPermissionController(BasePermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Operation(description = "count permissions")
    @GetMapping(path = SecurityPermissionRestApi.PERMISSIONS_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> count(@Valid PermissionFilter filter,
                                         BindingResult bindingResult, HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(permissionService.count(filter), HttpStatus.OK);
    }

    @Operation(description = "get all permissions")
    @GetMapping(path = SecurityPermissionRestApi.PERMISSIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PermissionOut>> getAll(@Valid PermissionPageableFilter pageableFilter,
                                                      BindingResult bindingResult, HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(permissionService.getAll(pageableFilter), HttpStatus.OK);
    }
}
