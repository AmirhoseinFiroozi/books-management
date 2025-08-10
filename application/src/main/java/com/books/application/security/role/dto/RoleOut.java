package com.books.application.security.role.dto;

import com.books.application.security.permission.entity.SecurityPermissionEntity;
import com.books.application.security.role.entity.SecurityRoleEntity;
import com.books.application.security.role.statics.RoleType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Getter
@Setter
public class RoleOut {
    private Integer id;
    private String name;
    private Boolean show;
    private RoleType type;
    private List<Integer> permissionIds;

    public RoleOut(SecurityRoleEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.show = entity.isShow();
        this.type = entity.getType();
        if (Hibernate.isInitialized(entity.getPermissions()) && !CollectionUtils.isEmpty(entity.getPermissions())) {
            this.permissionIds = entity.getPermissions().stream().map(SecurityPermissionEntity::getId).toList();
        }
    }
}
