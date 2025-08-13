package com.books.domain.security.permission.entity;

import com.books.domain.security.permission.statics.PermissionType;
import com.books.domain.security.rest.entity.SecurityRestEntity;
import com.books.domain.security.role.entity.SecurityRoleEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "SECURITY_PERMISSION")
@Getter
@Setter
public class SecurityPermissionEntity {
    @Id
    @Column(name = "ID_PK")
    private int id;
    @Basic
    @Column(name = "PARENT_ID_FK")
    private Integer parentIdFk;
    @Basic
    @Column(name = "NODE_TYPE", nullable = false)
    private int nodeType;
    @Basic
    @Column(name = "TRAVERSAL", nullable = false)
    private boolean traversal;
    @Basic
    @Column(name = "NAME", length = 200)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PermissionType type;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "SECURITY_PERMISSION_REST",
            joinColumns = @JoinColumn(name = "PERMISSION_ID_FK", referencedColumnName = "ID_PK", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "REST_ID_FK", referencedColumnName = "ID_PK", nullable = false))
    private Set<SecurityRestEntity> rests;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    private Set<SecurityRoleEntity> roleEntities;
}
