package com.books.application.security.realm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "SECURITY_REALM", schema = "map")
@Getter
@Setter
public class SecurityRealmEntity {
    @Id
    @Column(name = "ID_PK")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Realm_Sequence")
    @SequenceGenerator(name = "Realm_Sequence", sequenceName = "SECURITY_REALM_SEQ", allocationSize = 1)
    private int id;
    @Basic
    @Column(name = "NAME", length = 100)
    private String name;
    @Basic
    @Column(name = "DELETED", nullable = false)
    private boolean deleted;
}
