package com.books.application.security.realm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "SECURITY_REALM")
@Getter
@Setter
@SQLRestriction("deleted is false")
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
