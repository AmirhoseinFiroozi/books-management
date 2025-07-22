package com.books.user.in.rolerealm.entity;

import com.books.company.entity.CompanyEntity;
import com.books.security.realm.entity.SecurityRealmEntity;
import com.books.security.role.entity.SecurityRoleEntity;
import com.books.user.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SECURITY_USER_ROLE_REALM", schema = "VRP")
@Getter
@Setter
@NoArgsConstructor
public class SecurityUserRoleRealmEntity {
    @Id
    @Column(name = "ID_PK")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserRoleRealm_Sequence")
    @SequenceGenerator(name = "UserRoleRealm_Sequence", sequenceName = "SECURITY_USER_ROLE_REALM_SEQ", allocationSize = 50)
    private int id;

    @Column(name = "ROLE_ID_FK", nullable = false)
    private int roleId;

    @Column(name = "REALM_ID_FK", nullable = false)
    private int realmId;

    @Column(name = "USER_ID_FK", nullable = false)
    private int userId;

    @Column(name = "COMPANY_ID_FK")
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID_FK", insertable = false, updatable = false)
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID_FK", referencedColumnName = "ID_PK", insertable = false, updatable = false)
    private SecurityRoleEntity role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REALM_ID_FK", referencedColumnName = "ID_PK", insertable = false, updatable = false)
    private SecurityRealmEntity realmEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID_FK", insertable = false, updatable = false)
    private CompanyEntity company;

    public SecurityUserRoleRealmEntity(int roleId, int realmId, int userId, Long companyId) {
        this.roleId = roleId;
        this.realmId = realmId;
        this.userId = userId;
        this.companyId = companyId;
    }
}
