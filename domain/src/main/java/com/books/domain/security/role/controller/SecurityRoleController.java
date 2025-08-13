package com.books.domain.security.role.controller;

import com.books.domain.security.role.dto.RoleFilter;
import com.books.domain.security.role.dto.RoleIn;
import com.books.domain.security.role.dto.RoleOut;
import com.books.domain.security.role.dto.RolePageableFilter;
import com.books.domain.security.role.service.BaseSecurityRoleService;
import com.books.domain.security.role.statics.constants.SecurityRoleRestApi;
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

@Tag(name = "Admin Role Controller")
@RestController
@RequestMapping(value = {"${rest.admin}"})
@Validated
public class SecurityRoleController {
    private final BaseSecurityRoleService roleService;

    @Autowired
    public SecurityRoleController(BaseSecurityRoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(description = "count roles")
    @GetMapping(path = SecurityRoleRestApi.ROLES_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> count(@Valid RoleFilter filter,
                                         BindingResult bindingResult, HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(roleService.count(filter), HttpStatus.OK);
    }

    @Operation(description = "get all roles")
    @GetMapping(path = SecurityRoleRestApi.ROLES, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleOut>> getAll(@Valid RolePageableFilter filter,
                                                BindingResult bindingResult, HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(roleService.getAll(filter), HttpStatus.OK);
    }

    @Operation(description = "create role")
    @PostMapping(path = SecurityRoleRestApi.ROLES, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleOut> create(@RequestBody @Valid RoleIn model, BindingResult bindingResult,
                                          HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(roleService.create(model), HttpStatus.OK);
    }

    @Operation(description = "update role")
    @PutMapping(path = SecurityRoleRestApi.ROLES_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleOut> update(@PathVariable(name = "id") int id, @RequestBody @Valid RoleIn model,
                                          BindingResult bindingResult, HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity<>(roleService.update(id, model), HttpStatus.OK);
    }

    @Operation(description = "delete role")
    @DeleteMapping(path = SecurityRoleRestApi.ROLES_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable(name = "id") int id, HttpServletRequest httpServletRequest) throws SystemException {
        roleService.delete(id);
    }
}
