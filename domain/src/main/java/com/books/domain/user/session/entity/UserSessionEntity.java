package com.books.domain.user.session.entity;

import com.books.domain.user.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "USER_SESSION")
@Getter
@Setter
public class UserSessionEntity {
    @Id
    @Column(name = "ID_PK")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Session_Sequence")
    @SequenceGenerator(name = "Session_Sequence", sequenceName = "USER_SESSION_SEQ", allocationSize = 1)
    private int id;

    @Column(name = "USER_ID_FK")
    private int userId;

    @JoinColumn(name = "USER_ID_FK", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @Column(name = "OS", length = 50, nullable = false)
    private String os;

    @Column(name = "AGENT", length = 400, nullable = false)
    private String agent;

    @Column(name = "CREATED", nullable = false)
    private LocalDateTime created;

    @Column(name = "IP", length = 39, nullable = false)
    private String ip;

    @PrePersist
    public void fillCreated() {
        this.created = LocalDateTime.now();
    }
}