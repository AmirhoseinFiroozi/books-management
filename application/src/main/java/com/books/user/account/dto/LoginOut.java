package com.books.user.account.dto;

import com.books.security.dto.TokenInfo;
import com.books.security.statics.RoleCategoryType;
import com.books.security.permission.entity.SecurityPermissionEntity;
import com.books.security.permission.statics.PermissionType;
import com.books.user.in.rolerealm.entity.SecurityUserRoleRealmEntity;
import com.books.user.user.entity.UserEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class LoginOut {
    @NotNull
    private AccountInfoOut account;
    private String accessToken;
    private String refreshToken;
    private Long ttl;
    private Long refreshTtl;
    private LocalDateTime creationTime;
    private Set<String> permissions;
    private boolean superAdmin;
    private boolean admin;

    public LoginOut(UserEntity userEntity, TokenInfo token) {
        this.account = new AccountInfoOut(userEntity);
        this.accessToken = token.getAccessToken();
        this.refreshToken = token.getRefreshToken();
        this.ttl = token.getTtl();
        this.refreshTtl = token.getRefreshTtl();
        this.creationTime = token.getCreationTime();
        this.permissions = new HashSet<>();
        if (Hibernate.isInitialized(userEntity.getRoleRealms()) && userEntity.getRoleRealms() != null) {
            for (SecurityUserRoleRealmEntity roleRealmEntity : userEntity.getRoleRealms()) {
                if (roleRealmEntity.getRole().getId() == RoleCategoryType.SUPER_ADMIN.getCode()) {
                    this.setSuperAdmin(true);
                }
                for (SecurityPermissionEntity permissionEntity : roleRealmEntity.getRole().getPermissions()) {
                    this.permissions.add(permissionEntity.getName());
                    if (!this.admin && permissionEntity.getType().equals(PermissionType.ADMIN)) {
                        this.admin = true;
                    }
                }
            }
        }
    }

}
