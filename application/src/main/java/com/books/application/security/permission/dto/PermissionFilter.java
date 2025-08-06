package com.books.application.security.permission.dto;

import com.books.application.security.permission.statics.PermissionType;
import com.books.utility.commons.repository.interfaces.FilterBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PermissionFilter implements FilterBase {
    private Integer id;
    private Integer parentIdFk;
    private Integer nodeType;
    private Boolean traversal;
    private String name;
    private PermissionType type;
}
