package com.books.application.user.user.dto;

import com.books.application.security.realm.dto.RealmOut;
import com.books.application.user.in.rolerealm.entity.SecurityUserRoleRealmEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

@Getter
@Setter
public class RoleRealmOut {
    private Integer roleId;
    private Integer realmId;
    private RoleOut role;
    private RealmOut realm;

    public RoleRealmOut(SecurityUserRoleRealmEntity entity) {
        if (entity != null) {
            this.roleId = entity.getRoleId();
            this.realmId = entity.getRealmId();
            if (Hibernate.isInitialized(entity.getRole()) && entity.getRole() != null) {
                this.role = new RoleOut(entity.getRole());
            }
            if (Hibernate.isInitialized(entity.getRealmEntity()) && entity.getRealmEntity() != null) {
                this.realm = new RealmOut(entity.getRealmEntity());
            }
        }
    }
}
