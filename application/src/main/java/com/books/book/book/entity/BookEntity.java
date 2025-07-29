package com.books.book.book.entity;

import com.books.book.shelf.entity.BookShelfEntity;
import com.books.user.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "BOOK")
@Getter
@Setter
@NoArgsConstructor
public class BookEntity {
    @Id
    @Column(name = "ID_PK")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Book_Sequence")
    @SequenceGenerator(name = "Book_Sequence", sequenceName = "BOOK_SEQ", allocationSize = 1)
    private int id;
    @Column(name = "NAME", length = 255)
    private String name;
    @Column(name = "CREATED", nullable = false)
    private LocalDateTime created;
    @Column(name = "FILE", length = 255, nullable = false)
    private String file;
    @Column(name = "USER_ID_FK", nullable = false)
    private int userId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID_FK", insertable = false, updatable = false)
    private UserEntity user;
    @Column(name = "BOOK_SHELF_ID_FK", nullable = false)
    private int bookShelfId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_SHELF_ID_FK", insertable = false, updatable = false)
    private BookShelfEntity bookShelf;

    @PrePersist
    public void fillCreated() {
        this.created = LocalDateTime.now();
    }
}
