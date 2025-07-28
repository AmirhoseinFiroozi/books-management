package com.books.user.user.entity;

import com.books.user.in.rolerealm.entity.SecurityUserRoleRealmEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {
    @Id
    @Column(name = "ID_PK")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "User_Sequence")
    @SequenceGenerator(name = "User_Sequence", sequenceName = "USER_SEQ", allocationSize = 1)
    private int id;

    @Column(name = "FULL_NAME", length = 201)
    private String fullName;

    @Column(name = "PHONE_NUMBER", length = 20)
    private String phoneNumber;

    @Column(name = "EMAIL", length = 254)
    private String email;

    @Column(name = "HASHED_PASSWORD", length = 100)
    private String hashedPassword;

    @Column(name = "LOCK_EXPIRED")
    private LocalDateTime lockExpired;

    @Column(name = "ACCESS_FAILED_COUNT", nullable = false)
    private int accessFailedCount;

    @Column(name = "CREATED", nullable = false)
    @CreationTimestamp
    private LocalDateTime created;

    @Column(name = "LAST_UPDATE")
    @UpdateTimestamp
    private LocalDateTime lastUpdate;

    @Column(name = "DELETED")
    private LocalDateTime deleted;

    @OneToMany(mappedBy = "userEntity", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SecurityUserRoleRealmEntity> roleRealms = new HashSet<>();
}
