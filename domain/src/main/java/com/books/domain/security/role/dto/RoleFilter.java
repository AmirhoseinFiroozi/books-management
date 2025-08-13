package com.books.domain.security.role.dto;

import com.books.domain.security.role.statics.RoleType;
import com.books.utility.commons.repository.interfaces.FilterBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleFilter implements FilterBase {
    private Integer id;
    private String name;
    private Boolean show;
    private RoleType type;
}
