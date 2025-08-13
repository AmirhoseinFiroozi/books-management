package com.books.domain.user.in.rolerealm.entity;

import com.books.domain.security.realm.entity.SecurityRealmEntity;
import com.books.domain.security.role.entity.SecurityRoleEntity;
import com.books.domain.user.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SECURITY_USER_ROLE_REALM")
@Getter
@Setter
@NoArgsConstructor
public class SecurityUserRoleRealmEntity {
    @Id
    @Column(name = "ID_PK")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserRoleRealm_Sequence")
    @SequenceGenerator(name = "UserRoleRealm_Sequence", sequenceName = "SECURITY_USER_ROLE_REALM_SEQ", allocationSize = 1)
    private int id;

    @Column(name = "ROLE_ID_FK", nullable = false)
    private int roleId;

    @Column(name = "REALM_ID_FK", nullable = false)
    private int realmId;

    @Column(name = "USER_ID_FK", nullable = false)
    private int userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID_FK", insertable = false, updatable = false)
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID_FK", referencedColumnName = "ID_PK", insertable = false, updatable = false)
    private SecurityRoleEntity role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REALM_ID_FK", referencedColumnName = "ID_PK", insertable = false, updatable = false)
    private SecurityRealmEntity realmEntity;

    public SecurityUserRoleRealmEntity(int roleId, int realmId, int userId) {
        this.roleId = roleId;
        this.realmId = realmId;
        this.userId = userId;
    }
}
