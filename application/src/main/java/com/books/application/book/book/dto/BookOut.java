package com.books.application.book.book.dto;

import com.books.application.book.book.entity.BookEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookOut {
    private int id;
    private String name;
    private LocalDateTime created;
    private Integer userId;

    public BookOut(BookEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.created = entity.getCreated();
        this.userId = entity.getUserId();
    }
}
