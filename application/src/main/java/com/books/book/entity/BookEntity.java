package com.books.book.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

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
    private long id;
    @Column(name = "NAME", length = 255)
    private String name;
    @Column(name = "CREATED", nullable = false)
    @CreationTimestamp
    private LocalDateTime created;
    @Column(name = "NAME", length = 255)
    private String file;
}
