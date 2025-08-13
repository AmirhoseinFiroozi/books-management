package com.books.domain.user.user.dto;

import com.books.domain.security.permission.entity.SecurityPermissionEntity;
import com.books.domain.security.permission.statics.PermissionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionOut {
    private int id;
    private Integer parentIdFk;
    private int nodeType;
    private PermissionType type;

    public PermissionOut(SecurityPermissionEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.parentIdFk = entity.getParentIdFk();
            this.nodeType = entity.getNodeType();
            this.type = entity.getType();
        }
    }
}
