package com.books.domain.user.user.dto;

import com.books.domain.security.role.statics.RoleType;
import com.books.utility.commons.repository.interfaces.FilterBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserFilter implements FilterBase {
    private Integer id;
    private String username;
    private String phoneNumber;
    private String email;
    private LocalDateTime lockExpireMin;
    private LocalDateTime lockExpireMax;
    private Boolean lock;
    private Boolean suspended;
    private LocalDateTime createdMin;
    private LocalDateTime createdMax;
    private Integer roleId;
    private RoleType type;
}
