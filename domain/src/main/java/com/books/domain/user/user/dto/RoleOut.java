package com.books.domain.user.user.dto;

import com.books.domain.security.role.entity.SecurityRoleEntity;
import com.books.domain.security.role.statics.RoleType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RoleOut {
    private int id;
    private String name;
    private RoleType type;
    private List<PermissionOut> permissions = new ArrayList<>();

    public RoleOut(SecurityRoleEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.name = entity.getName();
            this.type = entity.getType();
            if (Hibernate.isInitialized(entity.getPermissions()) && !CollectionUtils.isEmpty(entity.getPermissions())) {
                this.permissions = entity.getPermissions().stream().map(PermissionOut::new).toList();
            }
        }
    }
}
