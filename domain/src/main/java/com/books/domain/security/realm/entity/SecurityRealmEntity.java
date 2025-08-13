package com.books.domain.security.realm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "SECURITY_REALM")
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
}
