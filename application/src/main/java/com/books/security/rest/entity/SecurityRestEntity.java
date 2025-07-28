package com.books.security.rest.entity;

import com.books.security.permission.entity.SecurityPermissionEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "SECURITY_REST", schema = "map")
@Getter
@Setter
public class SecurityRestEntity {
    @Id
    @Column(name = "ID_PK")
    private int id;
    @Basic
    @Column(name = "HTTP_METHOD", length = 10, nullable = false)
    private String httpMethod;
    @Basic
    @Column(name = "URL", length = 200, nullable = false)
    private String url;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "rests")
    private Set<SecurityPermissionEntity> permissions;
}
