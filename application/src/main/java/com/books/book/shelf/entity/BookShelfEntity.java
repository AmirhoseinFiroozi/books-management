package com.books.book.shelf.entity;

import com.books.user.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "BOOK_SHELF")
@Getter
@Setter
@NoArgsConstructor
public class BookShelfEntity {
    @Id
    @Column(name = "ID_PK")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Book_Shelf_Sequence")
    @SequenceGenerator(name = "Book_Shelf_Sequence", sequenceName = "BOOK_SHELF_SEQ", allocationSize = 1)
    private int id;
    @Column(name = "NAME", length = 255)
    private String name;
    @Column(name = "CREATED", nullable = false)
    private LocalDateTime created;
    @Column(name = "USER_ID_FK", nullable = false)
    private int userId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID_FK", insertable = false, updatable = false)
    private UserEntity user;
}
