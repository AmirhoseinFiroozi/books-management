package com.books.application.book.shelf.dto;

import com.books.application.book.shelf.entity.BookShelfEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookShelfOut {
    private Integer id;
    private String name;
    private LocalDateTime created;
    private Integer userId;

    public BookShelfOut(BookShelfEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.created = entity.getCreated();
        this.userId = entity.getUserId();
    }
}
