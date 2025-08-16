package com.books.search.dto;

import com.books.domain.book.book.entity.BookEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookSearchOut {
    private int id;
    private String name;
    private LocalDateTime created;
    private String username;

    public BookSearchOut(BookEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.created = entity.getCreated();
        if (Hibernate.isInitialized(entity.getUser()) && entity.getUser() != null) {
            this.username = entity.getUser().getUsername();
        }
    }
}
