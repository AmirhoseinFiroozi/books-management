package com.books.application.security.permission.dto;

import com.books.application.security.permission.entity.SecurityPermissionEntity;
import com.books.application.security.permission.statics.PermissionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionOut {
    private Integer id;
    private Integer parentIdFk;
    private Integer nodeType;
    private Boolean traversal;
    private String name;
    private PermissionType type;

    public PermissionOut(SecurityPermissionEntity entity) {
        this.id = entity.getId();
        this.parentIdFk = entity.getParentIdFk();
        this.nodeType = entity.getNodeType();
        this.traversal = entity.isTraversal();
        this.name = entity.getName();
        this.type = entity.getType();
    }
}
