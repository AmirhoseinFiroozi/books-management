package com.books.domain.user.user.dto;

import com.books.domain.user.user.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserOut {
    private Integer id;
    private String username;
    private String phoneNumber;
    private String email;
    private LocalDateTime lockExpired;
    private Integer accessFailedCount;
    private Boolean suspended;
    private LocalDateTime created;
    private LocalDateTime lastUpdate;
    private List<RoleRealmOut> roles = new ArrayList<>();

    public UserOut(UserEntity entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.phoneNumber = entity.getPhoneNumber();
        this.email = entity.getEmail();
        this.lockExpired = entity.getLockExpired();
        this.accessFailedCount = entity.getAccessFailedCount();
        this.suspended = entity.isSuspended();
        this.created = entity.getCreated();
        this.lastUpdate = entity.getLastUpdate();
        if (Hibernate.isInitialized(entity.getRoleRealms()) && !CollectionUtils.isEmpty(entity.getRoleRealms())) {
            this.roles = entity.getRoleRealms().stream().map(RoleRealmOut::new).toList();
        }
    }
}
